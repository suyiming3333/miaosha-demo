package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.rabbitmq.MQSender;
import com.sym.miaoshaodemo.rabbitmq.MiaoshaMessage;
import com.sym.miaoshaodemo.redis.key.GoodKey;
import com.sym.miaoshaodemo.redis.key.OrderKey;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.result.CodeMsg;
import com.sym.miaoshaodemo.result.Result;
import com.sym.miaoshaodemo.service.GoodsService;
import com.sym.miaoshaodemo.service.MiaoShaService;
import com.sym.miaoshaodemo.service.OrderService;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: MiaoShaController
 * @Package com.sym.miaoshaodemo.controller
 * @Description: TODO
 * @date 2019/9/15 21:59
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {

    @Autowired
    private GoodsService goodService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoShaService miaoShaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    MQSender sender;

    /**用于标记商品是否秒杀玩了，目的是减少redis的访问**/
    private HashMap<Integer, Boolean> localOverMap =  new HashMap<Integer, Boolean>();

    /**
     * controller实例化的时候初始化产品库存
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //获取所有商品列表
        List<GoodsVo> list = goodService.getAllGoodList(0);
        //遍历list
        for(GoodsVo goodsVo : list){
            //设置redis缓存
            redisService.set(GoodKey.goodStock,String.valueOf(goodsVo.getId()),goodsVo.getStockCount());
            //本地内存标记，商品是否秒杀完
            localOverMap.put(goodsVo.getId(),false);
        }

        System.out.println("init goods stock over");
    }


    /**
     * QPS 700/s 本地环境
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/do_miaosha")
    @ResponseBody
    public Result<Integer> doMiaoSha(Model model,
                                     MiaoshaUser user,
                                     @RequestParam("goodsId")int goodsId){

        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean isOver = localOverMap.get(goodsId);
        if(isOver){
            //本地内存已标记该商品秒杀完了
            return Result.error(CodeMsg.SELL_OVER);
        }
        //预减库存
        int stock = redisService.decr(GoodKey.goodStock,String.valueOf(goodsId)).intValue();
        if(stock<0){
            //预减库存不足
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SELL_OVER);
        }

        //判断用户是否已经秒杀过这个商品(redis)
        OrderInfo orderInfo = redisService.get(OrderKey.msOrder,user.getId()+"-"+goodsId,OrderInfo.class);
        if(orderInfo!=null){
            return Result.error(CodeMsg.REPEATED_BUY);
        }

        //发送消息入队列
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0);//排队中
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method= RequestMethod.GET)
    @ResponseBody
    public Result<Integer> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")int goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Integer result  =miaoShaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }


    @RequestMapping("/do_miaosha2")
    public String doMiaoSha2(Model model,
                            MiaoshaUser user,
                            @RequestParam("goodsId")int goodsId){

        model.addAttribute("user", user);
        if(user == null) {
            return "login";
        }

        //判断库存数量
        List<GoodsVo> list = goodService.getAllGoodList(goodsId);
        GoodsVo goodsVo = list.get(0);
        /**
         * stock =1,此时有同一个用户，有两个request进来，继续往下执行
         * (解决同一个用户同时有两个请求的过来的方法：秒杀前要输入验证码)
         *
         */
        if(goodsVo.getStockCount()<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        /**
         * 此时该用户还没创建过秒杀订单，继续往下执行
         * (可以通过goodsId userId 唯一索引去控制)
         * create unique index index_user_good on ms_order_ref (userId,goodsId)
         */
        //判断是否已经秒杀过
        Map<String,Object> orderMap = orderService.getExistsMiaoShaOrder(user.getId(),goodsId);
        if(orderMap != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        /**
         * 同时减库存，下订单，创建秒杀订单，导致超卖
         */
        //减库存、记录秒杀订单，下订单(需要再同一个事务执行)
        OrderInfo orderInfo = miaoShaService.doMiaoSha(user,goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }

}
