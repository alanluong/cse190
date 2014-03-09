package com.example.bidit;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class Constants {

	public static final String[] IMAGES = new String[] {
			// Heavy images
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/29.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/30.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/31.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/32.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/33.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/34.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/35.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/36.jpg",
            "http://ec2-54-213-102-70.us-west-2.compute.amazonaws.com/uploads/37.jpg",
            
			// Special cases

	};

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String IMAGES = "com.example.bidit.IMAGES";
		public static final String IMAGE_POSITION = "com.example.bidit.IMAGE_POSITION";
	}
}
