package com.bluedavy.rpc;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ServerHandlerUtil {

	// Server Processors     key: servicename    value: service instance
	private static Map<String, Object> processors = new HashMap<String, Object>();
	
	// Cached Server Methods  key: instanceName#methodname$argtype_argtype
	private static Map<String, Method> cacheMethods = new HashMap<String, Method>();
	
	public static void registerProcessor(String instanceName,Object instance){
		processors.put(instanceName, instance);
		Class<?> instanceClass = instance.getClass();
		Method[] methods = instanceClass.getMethods();
		for (Method method : methods) {
			Class<?>[] argTypes = method.getParameterTypes();
			StringBuilder methodKeyBuilder = new StringBuilder();
			methodKeyBuilder.append(instanceName).append("#");
			methodKeyBuilder.append(method.getName()).append("$");
			for (Class<?> argClass : argTypes) {
				methodKeyBuilder.append(argClass.getName()).append("_");
			}
			cacheMethods.put(methodKeyBuilder.toString(), method);
		}
	}
	
	public static ResponseWrapper handleRequest(final RequestWrapper request){
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setRequestId(request.getId());
		String targetInstanceName = request.getTargetInstanceName();
		String methodName = request.getMethodName();
		String[] argTypes = request.getArgTypes();
		Object[] requestObjects = null;
		Method method = null;
		try{
			Object processor = processors.get(targetInstanceName);
			if(processor == null){
				throw new Exception("no "+targetInstanceName+" instance exists on the server");
			}
			if (argTypes != null && argTypes.length > 0) {
				StringBuilder methodKeyBuilder = new StringBuilder();
				methodKeyBuilder.append(targetInstanceName).append("#");
				methodKeyBuilder.append(methodName).append("$");
				Class<?>[] argTypeClasses = new Class<?>[argTypes.length];
				for (int i = 0; i < argTypes.length; i++) {
					methodKeyBuilder.append(argTypes[i]).append("_");
					argTypeClasses[i] = Class.forName(argTypes[i]);
				}
				requestObjects = new Object[argTypes.length];
				method = cacheMethods.get(methodKeyBuilder.toString());
				Object[] tmprequestObjects = request
						.getRequestObjects();
				for (int i = 0; i < tmprequestObjects.length; i++) {
					ObjectInputStream objectIn = new ObjectInputStream(
							new ByteArrayInputStream(
									(byte[]) tmprequestObjects[i]));
					requestObjects[i] = objectIn.readObject();
					objectIn.close();
				}
			} 
			else {
				method = processor.getClass().getMethod(methodName,
						new Class<?>[] {});
				requestObjects = new Object[] {};
			}
			responseWrapper.setResponse(method.invoke(processor, requestObjects));
		}
		catch(Exception e){
			responseWrapper.setException(e);
		}
		return responseWrapper;
	}
}
