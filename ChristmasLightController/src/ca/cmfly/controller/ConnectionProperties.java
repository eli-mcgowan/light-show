package ca.cmfly.controller;

/**
 * Replace with properties file.
 *
 */
public class ConnectionProperties {

	private static final String ENV = ConnectionProperties.ENV_CHRIS_LIVE;
//	private static final String ENV = ConnectionProperties.ENV_CHRIS_TEST;
	
	private static final String ENV_CHRIS_LIVE = "ENV_CHRIS_LIVE";
	private static final String ENV_CHRIS_TEST = "ENV_CHRIS_TEST";
	private static final String ENV_ELI = "ENV_ELI";
	
	private static final String CHRIS_LIVE_HOST = "192.168.1.61";
	private static final String CHRIS_TEST_HOST = "192.168.1.63";
	private static final String ELI_HOST = "localhost";

	private static final int CHRIS_PORT = 8888;
	private static final int ELI_PORT = 7654;
	
	public static String getHost(){
		if(ENV.equals(ENV_CHRIS_LIVE)){
			return CHRIS_LIVE_HOST;
		} else if(ENV.equals(ENV_CHRIS_TEST)){
			return CHRIS_TEST_HOST;
		} else if(ENV.equals(ENV_ELI)){
			return ELI_HOST;
		}
		throw new RuntimeException("Unknown Environment: " + ENV);
	}
	
	public static int getPort(){
		if(ENV.equals(ENV_CHRIS_LIVE)){
			return CHRIS_PORT;
		} else if(ENV.equals(ENV_CHRIS_TEST)){
			return CHRIS_PORT;
		} else if(ENV.equals(ENV_ELI)){
			return ELI_PORT;
		}
		throw new RuntimeException("Unknown Environment: " + ENV);
	}
	
	public static boolean isLive(){
		if(ENV.equals(ENV_CHRIS_LIVE)){
			return true;
		} else if(ENV.equals(ENV_CHRIS_TEST)){
			return false;
		} else if(ENV.equals(ENV_ELI)){
			return true;
		}
		throw new RuntimeException("Unknown Environment: " + ENV);
	}
}
