package com.sym.miaoshaodemo.controller;


import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.exception.GlobalException;
import com.sym.miaoshaodemo.redis.key.GoodKey;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.result.CodeMsg;
import com.sym.miaoshaodemo.service.GoodsService;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
    private GoodsService goodService;
	
	@Autowired
	private RedisService redisService;

	@Autowired
	private ApplicationContext applicationContext;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * @description: TODO　　
     * @params
     * @param model
     * @param user webconfig里添加的自定义参数，便于获取用户信息
     * @return java.lang.String
     * @throws
     * @author suyiming3333
     * @date 2019/5/4 15:23
     */
    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser user) {
        List<GoodsVo> list = goodService.getAllGoodList(0);
    	model.addAttribute("user", user);
        model.addAttribute("goodsList", list);
        return "goods_list";
    }


    @RequestMapping(value="/to_list2", produces="text/html")
    @ResponseBody
    public String list2(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("user", user);
        //从redis取缓存
        String html = redisService.get(GoodKey.goodList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            System.out.println("从redis读取商品列表页面");
            return html;
        }
        //否则从数据库里面重新取
        List<GoodsVo> list = goodService.getAllGoodList(0);
        model.addAttribute("goodsList", list);

        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );

        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        //设置缓存
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodKey.goodList,"",html);
            System.out.println("从数据库读取商品列表页面");
        }
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}",produces="text/html")
    @ResponseBody
    public String detail(Model model,MiaoshaUser user,
                         HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("goodsId")int goodsId) {
        //先从缓存里面取
        String goodDetailHtml = redisService.get(GoodKey.goodDetail,String.valueOf(goodsId),String.class);
        if(!StringUtils.isEmpty(goodDetailHtml)){
            System.out.println("get data from redis");
            return goodDetailHtml;
        }
        //再从数据库里面取
        List<GoodsVo> goodList = goodService.getAllGoodList(goodsId);
        if(goodList.size()<0){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        GoodsVo goods = goodList.get(0);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );

        //手动渲染
        goodDetailHtml = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(goodDetailHtml)){
            redisService.set(GoodKey.goodDetail,String.valueOf(goodsId),goodDetailHtml);
        }
        System.out.println("get data from db");
        return goodDetailHtml;
    }

    @RequestMapping("/getListFromRedis")
    @ResponseBody
    public String getListFromRedis() {
        return redisService.get(GoodKey.token,"type1",String.class);
    }
    
}
