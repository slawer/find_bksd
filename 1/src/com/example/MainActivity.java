package com.example;



import com.ftdi.j2xx.*;

import android.content.Context;

import com.ftdi.j2xx.D2xxManager.D2xxException;
import com.ftdi.*;
import com.ftdi.j2xx.D2xxManager;

import android.os.Bundle;

import android.app.Activity;
import android.text.InputFilter.LengthFilter;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle ;
import android.util.Log;
import android.os.AsyncTask;
import android.widget.ScrollView;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;

public class MainActivity extends Activity {
	
	 TextView tv;
	Button button;
	Handler h;
	MyTask mt;
	private boolean isRunning=false;
	private Handler mНandler;
	ScrollView myScroll;


	
    D2xxManager  ftdid2xx=null;
    Context myContext;
    int devCount;
    FT_Device ftDevice = null;
    int l=100;
    static Context DeviceUARTContext;
    int openIndex = 0;
    char[] readDataToText;
    public int iavailable = 0;
    byte[] readData;
    
    byte[] zap1={(byte)0x7e,(byte)0x01,(byte)0x07,(byte)0x02,(byte)0x00,(byte)0x0e,(byte)0xb0,(byte)0x38};

    int[]Crc16= {
            0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
            0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
            0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
            0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
            0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
            0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
            0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
            0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
            0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
            0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
            0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
            0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
            0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
            0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
            0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
            0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
            0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
            0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
            0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
            0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
            0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
            0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
            0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
            0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
            0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
            0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
            0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
            0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
            0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
            0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
            0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
            0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040};

    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        //    progress.incrementProgressBy(1);
            tv.setText("Progress: %");
        }
    }; 
    
   
  
    private byte[]  find_sum(byte[] buf){
        int sum=0;
        for (int i = 1; i < buf.length-1; i++) {
        	sum =sum + buf[i];
       // 	tv.setText(tv.getText()+"\n"+String.valueOf(i)+" "+String.valueOf(buf[i]) );
        }
    //	buf[buf.length-2]=(byte) (crc/256);
    	buf[buf.length-1]=(byte)(sum % 256);
    	
        return buf;
    }
    
    private byte[] find_crc(byte[] buf){
        int crc=0xFFFF;
        for (int i = 1; i < buf.length-2; i++) {
            crc = (crc >> 8) ^ Crc16[(crc & 0xFF) ^ buf[i]];
       // 	tv.setText(tv.getText()+"\n"+String.valueOf(i)+" "+String.valueOf(buf[i]) );
        }
    	buf[buf.length-2]=(byte) (crc/256);
    	buf[buf.length-1]=(byte)(crc % 256);
        return buf;
    }

    
    public void proc(){
    	
	//     final TextView tv = (TextView) findViewById(R.id.textView1);
	     //    TextView tv = new TextView(this); 
       tv.setText("start proc"); 
       myScroll.fullScroll(View.FOCUS_DOWN);
    	
    //	msg = h.obtainMessage(1);
        // отправляем
    //    h.sendMessage(msg);
     //   h.sendEmptyMessage(1);
       
        //setContentView(tv); 

			long endTime = System.currentTimeMillis() + 10*1000;

			byte[] data1 = new byte [] {(byte)0xFF};
			byte[] data2 = new byte [] {(byte)0x00};
			
			byte[] data3 = new byte [] {(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08,(byte)0x09,(byte)0x0A};
			
			
			try {
				ftdid2xx = D2xxManager.getInstance(myContext);
			} catch (D2xxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			devCount = ftdid2xx.createDeviceInfoList(DeviceUARTContext);
			
			// open our first device
			tv.setText(tv.getText()+"\n"+"finded "+String.valueOf(devCount)+" device"); 
			
			if (devCount>0)
			{
		//	ftDevice = ftdid2xx.openByIndex(myContext, 0);
			ftDevice = ftdid2xx.openByIndex(DeviceUARTContext, openIndex);
			
			// configure our port , Set to ASYNC BIT MODE
		//	ftDevice.setBitMode((byte) 0xFF, D2xxManager.FT_BITMODE_ASYNC_BITBANG);
			ftDevice.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

			// configure Baud rate
			ftDevice.setBaudRate(38400);
			ftDevice.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);	
			ftDevice.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte) 0x0b, (byte) 0x0d);

			long nach=System.currentTimeMillis();
			byte kol=0;
	   // 	while (System.currentTimeMillis() < endTime) {
	    	while (kol<20){
	    		
	 		    	//	synchronized (this) 
	 		    		{
	 		    			try {

	 		    				zap1[1]=(byte) (kol % 10);
	 		    				if (zap1[1]==0)
	 		    					zap1[1]=0x3E;
	 		    				if (kol>9)
	 		    					zap1=find_sum(zap1);	 		    				
	 		    				else
	 		    					zap1=find_crc(zap1);
	 		    				kol+=1;
	 		 					
	 		 					
	 		    			//	ftDevice.write(data3, 10);
	 		    				String st="";
	 		    				readDataToText = new char[zap1.length];
	 		    				for (int i = 0; i < zap1.length; i++) {
	 		    					String s=Integer.toHexString((int)zap1[i]).toUpperCase()+" ";
	 		    					if (s.length()>3)
	 		    						s=s.substring(s.length()-3,s.length());
	 			    				while (s.length()<3)				
	 			    					s="0"+s;
	 			    				st+=s;
	   							}	    				
	 		    				
	 		    				ftDevice.write(zap1, zap1.length);
	 		    				
	 		    				tv.setText(tv.getText()+"\n	"+String.valueOf(System.currentTimeMillis()-nach)+" ms,	write "+String.valueOf(zap1.length)+" bait: "+st); 
	 		    				tv.refreshDrawableState();
	 		    				nach=System.currentTimeMillis();
	 		      	    		
	 		    				Thread.sleep(100);

	 		      	    		iavailable = ftDevice.getQueueStatus();	
	 		      	    		
	       						if (iavailable > 0) {      							
	       							readData = new byte[iavailable];
	       							ftDevice.read(readData, iavailable);
	       							/*
	       							readDataToText = new char[iavailable];
	       							for (int i = 0; i < iavailable; i++) {
	       								readDataToText[i] = (char) readData[i];
	       							}
	       							*/
	       							
	     		    				String st1="";    		    				
	     		    				for (int i = 0; i < iavailable; i++) {
	     		    					String s=Integer.toHexString((int)readData[i]).toUpperCase()+" ";
	     		    					if (s.length()>3)
	     		    						s=s.substring(s.length()-3,s.length());
	     			    				while (s.length()<3)				
	     			    					s="0"+s;
	     			    				st1+=s;
	       							}     							
	       							tv.setText(tv.getText()+"\n	"+String.valueOf(System.currentTimeMillis()-nach)+"	read "+String.valueOf(iavailable)+" bait, "+st1);         						
	       						}
	       						
	       						
	       						
	 		      	    		/*
	 		      				while(true == bReadThreadGoing)
	 		      				{
	 		      					try {
	 		      						Thread.sleep(50);
	 		      					} catch (InterruptedException e) {
	 		      					}

	 		      					synchronized(ftDev)
	 		      					{
	 		      						iavailable = ftDev.getQueueStatus();				
	 		      						if (iavailable > 0) {
	 		      							
	 		      							if(iavailable > readLength){
	 		      								iavailable = readLength;
	 		      							}
	 		      							
	 		      							ftDev.read(readData, iavailable);
	 		      							for (i = 0; i < iavailable; i++) {
	 		      								readDataToText[i] = (char) readData[i];
	 		      							}
	 		      							Message msg = mHandler.obtainMessage();
	 		      							mHandler.sendMessage(msg);
	 		      						}
	 		      					}
	 		      				}
	 		      				*/
	 		      	    		
	 		      	    		
	 		      	  		
	 		    			} catch (Exception e) {
	 		    				e.printStackTrace();
	 		    			}
	 		    		}
	 		    	}

	 				ftDevice.close();
	 				}
	 				else {
	 					tv.setText(tv.getText()+"\n exit");   
			/*
	 					int c=find_crc(zap1);
	 					int c1=c/256;
	 					int c2=c % 256;
	 					
	 					String st2="";

    					String s=Integer.toHexString(c1).toUpperCase()+" ";
    					if (s.length()>3)
    						s=s.substring(s.length()-3,s.length());
	    				while (s.length()<3)				
	    					s="0"+s;
	    				st2+=s;
	
    					s=Integer.toHexString(c2).toUpperCase()+" ";
    					if (s.length()>3)
    						s=s.substring(s.length()-3,s.length());
	    				while (s.length()<3)				
	    					s="0"+s;
	    				st2+=s;
	    				
	    				
	 					tv.setText(tv.getText()+"\n	"+st2); 
	 		    	*/	
	 			 				
	 					/*
	 					String st="";
	    				readDataToText = new char[zap1.length];
	    				for (int i = 0; i < zap1.length; i++) {
	    					String s=Integer.toHexString((int)zap1[i]).toUpperCase()+" ";
	    					if (s.length()>3)
	    						s=s.substring(s.length()-3,s.length());
		    				while (s.length()<3)				
		    					s="0"+s;
		    				st+=s;
						}	    				
*/
	    				
	    						
	 				}
	 				isRunning = false;
    	
    	
    };
    
    
  
    
    void downloadFile() {
        // пауза - 1 секунда
        try {
        	Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    
    public void find_dev()
    { 
   // 	super.find_dev();
			myContext = this;
			DeviceUARTContext=this;   	
    	
	//		proc();
			
		       // Создаем новый поток
	        Thread background = new Thread(new Runnable() {
	            public void run() {
                    try {
                    	 
                       proc();
                    	Thread.sleep(1);
                    //	 isRunning = false;
                    //	downloadFile();
                        // обновляем TextView
                    	
                    	
                    } catch (InterruptedException e) {
                       
                    }
	            	/*
	                while (isRunning) {
	                    try {
	                    //    proc();
	                    	Thread.sleep(1);
	                    //	 isRunning = false;
	                    } catch (InterruptedException e) {
	                        Log.e("ERROR", "Thread Interrupted");
	                    }
	               //     handler.sendMessage(handler.obtainMessage());
	                
	                
	                }
	                */
	            }
	        });
        isRunning = true;
        background.start();
        
        tv.setText(tv.getText()+"\n	finish ");
        
    
    //  test	
  
    };
    
    class MyTask extends AsyncTask<String, String , Void> {

        @Override
        protected void onPreExecute() {
          super.onPreExecute();
          tv.setText( tv.getText()+"\n"+"Begin");
          myScroll.fullScroll(View.FOCUS_DOWN);
      //    proc();
        }

        @Override
        protected Void doInBackground(String... urls) {
          try {
           Thread.sleep(1);
          

           //    tv.setText("start proc"); 
               publishProgress("\n starting finded");

        			long endTime = System.currentTimeMillis() + 10*1000;

        			byte[] data1 = new byte [] {(byte)0xFF};
        			byte[] data2 = new byte [] {(byte)0x00};
        			
        			byte[] data3 = new byte [] {(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08,(byte)0x09,(byte)0x0A};
        			
        			
        			try {
        				ftdid2xx = D2xxManager.getInstance(myContext);
        			} catch (D2xxException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			

        			devCount = ftdid2xx.createDeviceInfoList(DeviceUARTContext);
        			
        			// open our first device
        		//	tv.setText(tv.getText()+"\n"+"finded "+String.valueOf(devCount)+" device"); 
        			publishProgress("\n"+"finded "+String.valueOf(devCount)+" device");
        			
        			if (devCount>0)
        			{
        		//	ftDevice = ftdid2xx.openByIndex(myContext, 0);
        			ftDevice = ftdid2xx.openByIndex(DeviceUARTContext, openIndex);
        			
        			// configure our port , Set to ASYNC BIT MODE
        		//	ftDevice.setBitMode((byte) 0xFF, D2xxManager.FT_BITMODE_ASYNC_BITBANG);
        			ftDevice.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

        			// configure Baud rate
        			ftDevice.setBaudRate(38400);
        			ftDevice.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);	
        			ftDevice.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte) 0x0b, (byte) 0x0d);

        			long nach=System.currentTimeMillis();
        			byte kol=0;
        	   // 	while (System.currentTimeMillis() < endTime) {
        	    	while (kol<20){
        	    		
        	 		    	//	synchronized (this) 
        	 		    		{
        	 		    			try {

        	 		    				zap1[1]=(byte) (kol % 10);
        	 		    				if (zap1[1]==0)
        	 		    					zap1[1]=0x3E;
        	 		    				if (kol>9)
        	 		    					zap1=find_sum(zap1);	 		    				
        	 		    				else
        	 		    					zap1=find_crc(zap1);
        	 		    				kol+=1;
        	 		 					
        	 		 					
        	 		    			//	ftDevice.write(data3, 10);
        	 		    				String st="";
        	 		    				readDataToText = new char[zap1.length];
        	 		    				for (int i = 0; i < zap1.length; i++) {
        	 		    					String s=Integer.toHexString((int)zap1[i]).toUpperCase()+" ";
        	 		    					if (s.length()>3)
        	 		    						s=s.substring(s.length()-3,s.length());
        	 			    				while (s.length()<3)				
        	 			    					s="0"+s;
        	 			    				st+=s;
        	   							}	    				
        	 		    				
        	 		    				ftDevice.write(zap1, zap1.length);
        	 		    				
        	 		    		//		tv.setText(tv.getText()+"\n	"+String.valueOf(System.currentTimeMillis()-nach)+" ms,	write "+String.valueOf(zap1.length)+" bait: "+st); 
        	 		    				publishProgress("\n	"+String.valueOf(System.currentTimeMillis()-nach)+" ms,	write "+String.valueOf(zap1.length)+" bait: "+st);
        	 		    				
        	 		    				
        	 		    				nach=System.currentTimeMillis();
        	 		      	    		
        	 		    				Thread.sleep(100);

        	 		      	    		iavailable = ftDevice.getQueueStatus();	
        	 		      	    		
        	       						if (iavailable > 0) {      							
        	       							readData = new byte[iavailable];
        	       							ftDevice.read(readData, iavailable);
        	       							/*
        	       							readDataToText = new char[iavailable];
        	       							for (int i = 0; i < iavailable; i++) {
        	       								readDataToText[i] = (char) readData[i];
        	       							}
        	       							*/
        	       							
        	     		    				String st1="";    		    				
        	     		    				for (int i = 0; i < iavailable; i++) {
        	     		    					String s=Integer.toHexString((int)readData[i]).toUpperCase()+" ";
        	     		    					if (s.length()>3)
        	     		    						s=s.substring(s.length()-3,s.length());
        	     			    				while (s.length()<3)				
        	     			    					s="0"+s;
        	     			    				st1+=s;
        	       							}     							
        	       							
        	     		    			//	tv.setText(tv.getText()+"\n	"+String.valueOf(System.currentTimeMillis()-nach)+"	read "+String.valueOf(iavailable)+" bait, "+st1);         						
        	     		    				publishProgress("\n	"+String.valueOf(System.currentTimeMillis()-nach)+"	read "+String.valueOf(iavailable)+" bait, "+st1);
        	       						}
        	       						
        	 		    			} catch (Exception e) {
        	 		    				e.printStackTrace();
        	 		    			}
        	 		    		}
        	 		    	}

        	 				ftDevice.close();
        	 				}
        	 				else {
        	 				//	tv.setText(tv.getText()+"\n exit");  
        	 					publishProgress("\n exit");			
        	 				}

          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          return null;
        }

        
        @Override
        protected void onProgressUpdate(String ...  s) {
          super.onProgressUpdate(s);
          for (String st : s) {
          tv.setText(tv.getText() + st);
          myScroll.fullScroll(View.FOCUS_DOWN);
    //      myScroll.scrollTo(0,myScroll.getBottom());
          }
  //        myScroll.scrollBy(0, -200);
 //         myScroll.scrollTo(0, myScroll.getBottom());

        //  tv.setText(tv.getText() + " \n go");
        }
        
        
        @Override
        protected void onPostExecute(Void result) {
          super.onPostExecute(result);
          tv.setText(tv.getText()+"\n"+"End");
    
          myScroll.fullScroll(View.FOCUS_DOWN);
       //   myScroll.scrollTo(0,myScroll.getBottom());
        }
      }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

		myContext = this;
		DeviceUARTContext=this; 
		
        tv = (TextView) findViewById(R.id.textView1);
        myScroll = (ScrollView) findViewById(R.id.myview);


        
        button = (Button) findViewById(R.id.go);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) // клик на кнопку
            {
             //   find_dev();
 
                mt = new MyTask();
                mt.execute();
                
              //  proc();
            }
        });
        
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
              // обновляем TextView
              tv.setText(tv.getText()+"\n Закачано файлов: " + msg.what);
              
              
            };
          };
        
        
        // test 1
        /*
        
		long endTime = System.currentTimeMillis() + 10*1000;

		byte[] data1 = new byte [] {(byte)0xFF};
		byte[] data2 = new byte [] {(byte)0x00};
		
		try {
			ftdid2xx = D2xxManager.getInstance(this);
		} catch (D2xxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		myContext = this;
		devCount = ftdid2xx.createDeviceInfoList(myContext);
		// open our first device
		tv.setText(tv.getText()+"\n"+"finded "+String.valueOf(devCount)+" device"); 
		
		if (devCount>0)
		{
		ftDevice = ftdid2xx.openByIndex(myContext, 0);
		// configure our port , Set to ASYNC BIT MODE
		ftDevice.setBitMode((byte) 0xFF, D2xxManager.FT_BITMODE_ASYNC_BITBANG);
		// configure Baud rate
		ftDevice.setBaudRate(9600);

    	while (System.currentTimeMillis() < endTime) {
    		synchronized (this) {
    			try {
    				ftDevice.write(data1, 1);
      	    		Thread.sleep(1000);
      	    		ftDevice.write(data2, 1);
      	    		Thread.sleep(1000);
      	    		tv.setText(tv.getText()+"\n"+"	write "); 
      	  		
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}

		ftDevice.close();
		}
		else
			tv.setText(tv.getText()+"\n exit"); 
		
		*/
		// test 2 
		/*
		  
				long endTime = System.currentTimeMillis() + 10*1000;

				byte[] data1 = new byte [] {(byte)0xFF};
				byte[] data2 = new byte [] {(byte)0x00};
				
				byte[] data3 = new byte [] {(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08,(byte)0x09,(byte)0x0A};
				
				
				try {
					ftdid2xx = D2xxManager.getInstance(this);
				} catch (D2xxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				myContext = this;
				devCount = ftdid2xx.createDeviceInfoList(myContext);
				// open our first device
				tv.setText(tv.getText()+"\n"+"finded "+String.valueOf(devCount)+" device"); 
				
				if (devCount>0)
				{
				ftDevice = ftdid2xx.openByIndex(myContext, 0);
				// configure our port , Set to ASYNC BIT MODE
				ftDevice.setBitMode((byte) 0xFF, D2xxManager.FT_BITMODE_ASYNC_BITBANG);
				// configure Baud rate
				ftDevice.setBaudRate(38400);

		    	while (System.currentTimeMillis() < endTime) {
		    		 
		    		synchronized (this) {
		    			try {

		    				
		    				ftDevice.write(data3, 10);
		    				
		    			//	tv.setText(tv.getText()+"\n"+"	write 10"); 
		      	    		Thread.sleep(10);
		      	    		
		      	    		
		      	  		
		    			} catch (Exception e) {
		    				e.printStackTrace();
		    			}
		    		}
		    	}

				ftDevice.close();
				}
				else
					tv.setText(tv.getText()+"\n exit"); 
		*/
        
        
        //test 3
        
        /*
		String st1="";
		readDataToText = new char[zap1.length];
		for (int i = 0; i < zap1.length; i++) {
			//	readDataToText[i] = Integer.toHexString(zap1[i]);
				
			//	st+=Integer.toHexString(zap1[i]).toUpperCase().format(format, args)+" ";
			String s=Integer.toHexString((int)zap1[i]).toUpperCase()+" ";
				if (s.length()>3)
						s=s.substring(s.length()-3,s.length());
				while (s.length()<3)				
					s="0"+s;
				st1+=s;
		
		//	st1+=Byte.toString(zap1[i]).toUpperCase()+" ";
			
			
				
		}
				
       
		tv.setText(tv.getText()+"\n"+st1); 
			*/		
		  
        // with errors
		/*
        
		try {
			ftdid2xx = D2xxManager.getInstance(this);
		} catch (D2xxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
		myContext = this;
		devCount = ftdid2xx.createDeviceInfoList(myContext);
		tv.setText(tv.getText()+"\n"+"finded "+String.valueOf(devCount)+" device"); 
		
		if (devCount>0)
		{
		
		long endTime = System.currentTimeMillis() + 10*1000;

		byte[] data1 = new byte [(int)l];
		byte[] data2 = {(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08,(byte)0x09};
		
		byte[] data3 = new byte [(byte)0x00];
		
		
		for (int i = 1; i <= l; i++) {
			data1[i]=(byte) i;
			}
		
		// open our first device
	//	ftDevice = ftdid2xx.openByIndex(myContext, 0);
		
		ftDevice = ftdid2xx.openByIndex(DeviceUARTContext, openIndex);
		
		// configure our port , Set to ASYNC BIT MODE
	//	ftDevice.setBitMode((byte) 0xFF, D2xxManager.FT_BITMODE_ASYNC_BITBANG);
		
		// configure our port
				// reset to UART mode for 232 devices
		ftDevice.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

		// configure Baud rate
    	ftDevice.setBaudRate(38400);
		 ftDevice.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);	
		 ftDevice.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte) 0x0b, (byte) 0x0d);

		

    	while (System.currentTimeMillis() < endTime) {
    		synchronized (this) {
    			try {

    				ftDevice.write(data3, 1);
    			//	tv.setText(tv.getText()+"\n"+"	"+String.valueOf(System.currentTimeMillis())+"	write "+data2);           	  		
      	    		Thread.sleep(1000);

    			} catch (Exception e) {
    			//	e.printStackTrace();
    				tv.setText(tv.getText()+"\n	error");           	  		
      	    		
    				
    			}
    		}
    	}

		ftDevice.close();
		}
		else
			tv.setText(tv.getText()+"\n"+"exit"); 
		*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
