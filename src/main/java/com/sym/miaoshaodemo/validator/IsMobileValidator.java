package com.sym.miaoshaodemo.validator;
import  javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sym.miaoshaodemo.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义参数校验器，实现类
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	private boolean required = false;
	
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtil.isMobile(value);
		}else {
			if(StringUtils.isEmpty(value)) {
				return true;
			}else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
