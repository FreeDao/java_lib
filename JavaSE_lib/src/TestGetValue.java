
public class TestGetValue {
	static int time = 5000;
	static int number = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Thread setNmuber = new Thread(){
			public void run(){
				
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					number++;
				}
					
			}
		};
		setNmuber.start();
		
		final Thread getStatusLoop = new Thread(){
			private boolean isContinue = true;
			
			public void stopT(){
				isContinue =false;
			}
			
			public void run(){
				while(isContinue){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					boolean result = getStatus();
					System.out.println("number=="+number+"   result===="+result);
					if(result){
						stopT();
						System.out.println("getStatusLoop ended result is :"+ result);
					}
				}
				
				
			}
		};
		getStatusLoop.start();
		
		
		final Thread timer = new Thread(){
			public void run(){
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean result = getStatus();
				System.out.println("timer is up===status :"+result);
				getStatusLoop.stop();
			}
		};
		timer.start();
		
		
		
		
		
		
	}
	
	private static boolean getStatus(){
		boolean status = false;
		int value = getValue();
		if(value>100){
			status = true;
		}else{
			status = false;
		}
		return status;
	}
	

	private static int getValue(){
		int value = number;
		
		return value;
		
	}

}
