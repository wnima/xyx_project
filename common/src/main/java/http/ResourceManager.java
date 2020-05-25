package http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Singleton;

import annotation.Path;
import inject.BeanManager;
import io.netty.handler.codec.http.FullHttpRequest;
import util.ASObject;
import util.ClassUtil;
import util.LogHelper;
import util.Pair;

@Singleton
public class ResourceManager {
	private Map<String, Pair<Object, Method>> methodMap = new HashMap<>();

	private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

	public static ResourceManager getInst() {
		return BeanManager.getBean(ResourceManager.class);
	}

	public void registerAllHttpHandlr(String packageName) {
		List<Class<?>> classList = ClassUtil.findAllClass(packageName);
		for (Class<?> classType : classList) {
			try {
				register(classType.newInstance());
			} catch (InstantiationException e) {
				// logger.error("", e);
				LogHelper.ERROR.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				// logger.error("", e);
				LogHelper.ERROR.error(e.getMessage(), e);
			}
		}
	}

	public void register(Object obj) {
		Path typePath = obj.getClass().getDeclaredAnnotation(Path.class);
		String classPath = typePath == null ? "" : typePath.value();
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			Path methodPath = method.getDeclaredAnnotation(Path.class);
			if (methodPath != null) {
				String path2Object = classPath + methodPath.value();
				methodMap.put(path2Object, new Pair<>(obj, method));
			}
		}
	}

	public String executeRequest(String action, FullHttpRequest request, String content) {
		if (action.contains("?")) {
			action = action.split("\\?")[0];
		}
		Pair<Object, Method> methodObj = methodMap.get(action);
		if (methodObj == null) {
			return null;
		}
		logger.info("the content is {}", content);
		ASObject params = null;
		try {
			params = new Gson().fromJson(content, ASObject.class);
		} catch (JsonSyntaxException e) {
			LogHelper.ERROR.error(e.getMessage(), e);
			return null;
		}
		try {
			return new Gson().toJson(methodObj.getRight().invoke(methodObj.getLeft(), params));
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}

	public String execute(String action, FullHttpRequest request, String content) {
		if (action.contains("?")) {
			action = action.split("\\?")[0];
		}
		Pair<Object, Method> methodObj = methodMap.get(action);
		if (methodObj == null) {
			return null;
		}
		logger.info("the content is {}", content);
		ASObject params = null;
		try {
			params = new Gson().fromJson(content, ASObject.class);
		} catch (JsonSyntaxException e) {
			LogHelper.ERROR.error(e.getMessage(), e);
			return null;
		}
		try {
			return new Gson().toJson(methodObj.getRight().invoke(methodObj.getLeft(), params));
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}

	public String execute(String action, Map<String, String> params) {
		if (action.contains("?")) {
			action = action.split("\\?")[0];
		}
		Pair<Object, Method> methodObj = methodMap.get(action);
		if (methodObj == null) {
			return null;
		}
		logger.info("the content is {}", params);
		try {
			return new Gson().toJson(methodObj.getRight().invoke(methodObj.getLeft(), params));
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}

}
