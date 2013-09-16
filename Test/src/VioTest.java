
public class VioTest {

	private   volatile static Integer i = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new Thread( ){
			

			@Override
			public void run() {
				this.setName("Thread 1===");
				synchronized (i) {
					System.out.println(Thread.currentThread().getName() + ":"
							+ i);

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					i += 3;
					System.out.println(Thread.currentThread().getName() + ":"
							+ i);

				}
				
				
			}
			
		}.start();
		
		
		new Thread( ){

			@Override
			public void run() {
				
				this.setName("Thread 2===");
				
				synchronized(i){
				System.out.println(Thread.currentThread().getName()+":"+i);
				
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				i -= 1;
				System.out.println(Thread.currentThread().getName()+":"+i);
				}
			}
			
		}.start();
		
		
		new Thread( ){

			@Override
			public void run() {
				
				this.setName("Thread 3===");
				while(true){
					try {
						Thread.sleep(201);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName()+":"+i);
				}
				
			}
			
		}.start();
		
		

	}

	
	
}
