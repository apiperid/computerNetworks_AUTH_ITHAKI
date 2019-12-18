/*
 *         DIKTYA YPOLOGISTWN II
 *         STUDENT : PIPERIDIS ANESTIS
 *         AEM : 8689
 *         SEMESTER : 8
 * 
 * 
 */


package ithaki;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.sound.sampled.*;


public class Virtual_Modem 
{

	public static void main(String[] args) throws IOException, LineUnavailableException 
	{
	  System.out.println("**********************************************************");
	  System.out.println("                 STARTING PROGRAM                         "); 
	  System.out.println("**********************************************************");
	  
	  //CODES FOR EACH JOB
      String echo_request_code_with_delay = "echo_request_code=E4389";
      String echo_request_code_without_delay = "echo_request_code=E0000";
      String echo_request_code_get_temperature = "echo_request_code=E4389=T00";
      String image_request_code_CAM1 = "image_request_code=M4878=CAM=FIX=UDP=1024";
      String image_request_code_CAM2 = "image_request_code=M4878=CAM=PTZ=UDP=1024";
      String audio_request_code_clip_DPCM = "audio_request_code=A4227=F960";
      String audio_request_code_clip_AQ_DPCM = "audio_request_code=A4227=AQ=F960";
      String audio_request_code_generator_DPCM = "audio_request_code=A4227=T960";
      String ithakicopter_code = "Ithakicopter_code=Q7560";
      String vehicle_OBD_II_code = "Vehicle_OBD-II_code=V0682=OBD=";  
      
      //PORTS IN WHICH WE WILL SEND PACKETS AND RECEIVE PACKETS
      int ServerPort = 38031;
      int ClientPort = 48031;   
      
      //ITHAKIS IP 
      byte[] hostIP =  { (byte)155,(byte)207,(byte)18,(byte)208 };
      
      /*
       * USE ONLY ONE FUNCTION AT A TIME
       * REMOVE THE COMMENTS FOR THIS JOB
       * 
       */
      
      //get_echo_packets(echo_request_code_with_delay,ServerPort,ClientPort,hostIP);
      //get_echo_packets(echo_request_code_without_delay,ServerPort,ClientPort,hostIP);
      //get_echo_packets_temperature(echo_request_code_get_temperature,ServerPort,ClientPort,hostIP);
      //get_image(image_request_code_CAM1,ServerPort,ClientPort,hostIP);
      //get_image(image_request_code_CAM2,ServerPort,ClientPort,hostIP);
      //get_vehicle_diagnostics(vehicle_OBD_II_code,ServerPort,ClientPort,hostIP);
      //get_audio_DPCM(audio_request_code_clip_DPCM,ServerPort,ClientPort,hostIP);
      //get_audio_DPCM(audio_request_code_generator_DPCM,ServerPort,ClientPort,hostIP);
      //get_audio_clip_AQ_DPCM(audio_request_code_clip_AQ_DPCM,ServerPort,ClientPort,hostIP);
      //ithaki_copter(ithakicopter_code,hostIP);
      
     
      
      System.out.println("**********************************************************");
      System.out.println("                 END OF PROGRAM                           ");
      System.out.println("**********************************************************"); 
      
	} 
	
	
	public static void get_echo_packets(String echo_request_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		int MINUTES=5;
		
		System.out.println("----------------------------------------------------------");
	    System.out.println("      Function : get_echo_packets             ");
	    System.out.println("----------------------------------------------------------");
	    System.out.println("This procedure will take "+MINUTES +" minutes");
	        
	    //start_time and end_time IS FOR THE 5 MINUTES COUNTING
	    long start_time,end_time,send_time,receive_time;
	    
		byte[] echo = echo_request_code.getBytes();
		
		//WE USE A SOCKET TO SEND A UDP PACKET AND ANOTHER SOCKET IN ORDER TO RECEIVE UDP PACKET
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(echo,echo.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		//3 SECONDS TIMEOUT
		Socket_Receive.setSoTimeout(3000);
		//HERE WE WILL SAVE THE RECEIVED UDP PACKET
		byte[] rxbuffer = new byte[32];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		//TWO FILES
		//echo_packets.txt -> WE SAVE THE ECHO AS STING
		//echo_packets_responses_in_milliseconds.txt -> WE SAVE THE RESPONSE IN MILLISECONDS
		PrintStream echo_file = new PrintStream(new FileOutputStream("echo_packets.txt"));
        PrintStream echo_file_responses = new PrintStream(new FileOutputStream("echo_packets_responses_in_milliseconds.txt"));
		
        //START COUNTING TIME
        start_time=System.currentTimeMillis();
		end_time=0;
		while(end_time-start_time<=MINUTES*60000)
		{
			send_time=System.currentTimeMillis();
			//SEND UDP PACKET
			Socket_Send.send(packet_send);
			try
			{
				//RECEIVE UDP PACKET
		      Socket_Receive.receive(packet_receive);
			}
			catch (Exception x)
			{
		      //System.out.println(x);
			  if((System.currentTimeMillis()-start_time)>=(60000*MINUTES))
		        break;
			}		
			receive_time=System.currentTimeMillis();
			String message = new String(rxbuffer,0,rxbuffer.length);
			//IF THE LENGTH IS THE EXPECTED
			if(message.length()==32)
			{
				//IF PACKET IS CORRECT SAVE IT TO FILE
				if((message.substring(0,6).equals("PSTART"))&&(message.substring(27,32).equals("PSTOP")))
	            {
	              //IF THE PACKET IS THE EXPECTED ONE THEN SAVE THE PACKET AND THE
	              //RESPONSE TIME TO FILES
	              echo_file_responses.println(Long.toString(receive_time-send_time));
	              echo_file.println(message);
	            }
			}
			//System.out.println(message);
			end_time=System.currentTimeMillis();
		}
		Socket_Send.close();
		Socket_Receive.close();
		echo_file_responses.close();
        echo_file.close();
        
		System.out.println("----------------------------------------------------------");
	    System.out.println("     End of Function : get_echo_packets       ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_echo_packets_temperature(String echo_request_code_temperature,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{	
		//EXACTLY WHAT WE HAVE DONE ABOVE BUT HERE WE TAKE ONLY ONE PACKET
		//FOR THE SENSOR WITH CODE T00 WHICH IS THE ONLY ONE IN USE

		System.out.println("----------------------------------------------------------");
	    System.out.println("      Function : get_echo_packets_temperature             ");
	    System.out.println("----------------------------------------------------------");
	     
		byte[] echo = echo_request_code_temperature.getBytes();
		boolean flag=false;
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(echo,echo.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(3000);
		
		byte[] rxbuffer = new byte[54];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		PrintStream echo_file = new PrintStream(new FileOutputStream("echo_packets_temperature.txt"));
		while(flag==false)
		{
			Socket_Send.send(packet_send);		
		    try
		    {
		      Socket_Receive.receive(packet_receive);
		      flag=true;
		    }
		    catch(Exception x)
		    {
		    	//System.out.println(x);
		    }
		    //System.out.println(flag);
		}
	
		String message = new String(rxbuffer,0,rxbuffer.length);
		if(message.length()==54)
		{
			if((message.substring(0,6).equals("PSTART"))&&(message.substring(49,54).equals("PSTOP")))
            {
              echo_file.println(message);
            }
		}
		//System.out.println(message);
		echo_file.close();
		Socket_Send.close();
		Socket_Receive.close();
     
		System.out.println("----------------------------------------------------------");
	    System.out.println("     End of Function : get_echo_packets_temperature       ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_image(String image_request_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		System.out.println("----------------------------------------------------------");
	    System.out.println("           Function : get_image                     ");
	    System.out.println("----------------------------------------------------------");
	    
		byte[] image = image_request_code.getBytes();
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(image,image.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(3000);
		//EACH INCOMING PACKET WILL BE 1024 BYTES
		byte[] rxbuffer = new byte[1024];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
			
		FileOutputStream image_CAM1 = null;
		image_CAM1 = new FileOutputStream("image.jpeg");
		Socket_Send.send(packet_send);	
		for(;;)
		{
		  try
	      {
			//RECEIVE THE INCOMING PACKET AND SAVE IT TO OUR .jpeg FILE
            Socket_Receive.receive(packet_receive);	
            for(int i=0;i<rxbuffer.length;i++)
            	image_CAM1.write((int)rxbuffer[i]);	
		  }
		  catch (Exception x)
		  {
		    //System.out.println(x);
		    break;
		   }
		}
		
		image_CAM1.close();
		Socket_Send.close();
		Socket_Receive.close();
		System.out.println("----------------------------------------------------------");
	    System.out.println("           End of Function : get_image               ");
	    System.out.println("----------------------------------------------------------");
	}
	
	public static void get_vehicle_diagnostics(String vehicle_OBD_II_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : get_vehicle_diagnostics                     ");
	    System.out.println("----------------------------------------------------------");
	   
	    //HERE WE HAVE ONE FUNCTION FOR EACH CHARACTERISTIC
	    get_vehicle_OBD_II_engine_rpm(vehicle_OBD_II_code,ServerPort,ClientPort,hostIP);
	    get_vehicle_OBD_II_intake_air_temperature(vehicle_OBD_II_code,ServerPort,ClientPort,hostIP);
	    get_vehicle_OBD_II_throttle_position(vehicle_OBD_II_code,ServerPort,ClientPort,hostIP);
	    get_vehicle_OBD_II_speed(vehicle_OBD_II_code,ServerPort,ClientPort,hostIP);
	    get_vehicle_OBD_II_coolant_temperature(vehicle_OBD_II_code,ServerPort,ClientPort,hostIP);
	   
		System.out.println("----------------------------------------------------------");
	    System.out.println(" End of Function : get_vehicle_diagnostics                ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_vehicle_OBD_II_intake_air_temperature(String vehicle_OBD_II_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		int MINUTES=5;
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : get_vehicle_OBD_II_intake_air_temperature   ");
	    System.out.println("----------------------------------------------------------");
	    System.out.println("This procedure will take "+MINUTES +" minutes");
	    
	    String intake_air_temperature_code="01 0F";	   
	    long start_time,end_time,receive_time;
	    float FORMULA;
	    
	    vehicle_OBD_II_code+=intake_air_temperature_code;
		byte[] vehicle = vehicle_OBD_II_code.getBytes();
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(vehicle,vehicle.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(3000);
		
		byte[] rxbuffer = new byte[8];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		PrintStream intake_air_temperature_file = new PrintStream(new FileOutputStream("intake_air_temperature.txt"));
        PrintStream intake_air_temperature_time = new PrintStream(new FileOutputStream("intake_air_temperature_time.txt"));
		
		start_time=System.currentTimeMillis();
		end_time=0;
		while(end_time-start_time<60000*MINUTES)
		{
		  Socket_Send.send(packet_send);
		  try
		  {
		    Socket_Receive.receive(packet_receive);
		  }
		  catch (Exception x)
		  {
		    //System.out.println(x);
			if((System.currentTimeMillis()-start_time)>=(60000*MINUTES))
	          break;
		  }  	
	      receive_time=System.currentTimeMillis();
	      
	      String message = new String(rxbuffer,0,rxbuffer.length);
	      //System.out.println(message);
	      //CHECK IF INCOMING PACKET IS CORRECT
	      if(message.substring(0,5).equals("41 0F"))
	      {
	    	  //CALCULATING THE FORMULA AND SAVE IT TO THE FILE
	    	  FORMULA = hex2decimal(message.substring(6,8))-40;
		      intake_air_temperature_time.println(Long.toString(receive_time-start_time));
		      intake_air_temperature_file.println(FORMULA); 
	      }           
          end_time=System.currentTimeMillis();
		} 

		intake_air_temperature_file.close();
		intake_air_temperature_time.close();
		Socket_Send.close();
		Socket_Receive.close();
		System.out.println("----------------------------------------------------------");
	    System.out.println("End of Function : get_vehicle_OBD_II_intake_air_temperature");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_vehicle_OBD_II_throttle_position(String vehicle_OBD_II_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		int MINUTES=5;
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : get_vehicle_OBD_II_throttle_position        ");
	    System.out.println("----------------------------------------------------------");
	    System.out.println("This procedure will take "+MINUTES +" minutes");
	    
	    String throttle_position_code="01 11";	   
	    long start_time,end_time,receive_time;
	    float FORMULA;
	    
	    vehicle_OBD_II_code+=throttle_position_code;
		byte[] vehicle = vehicle_OBD_II_code.getBytes();
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(vehicle,vehicle.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(4000);
		
		byte[] rxbuffer = new byte[8];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		PrintStream throttle_position_file = new PrintStream(new FileOutputStream("throttle_position.txt"));
        PrintStream throttle_position_time = new PrintStream(new FileOutputStream("throttle_position_time.txt"));
		
		start_time=System.currentTimeMillis();
		end_time=0;
		while(end_time-start_time<60000*MINUTES)
		{
		  Socket_Send.send(packet_send);
		  try
		  {
		    Socket_Receive.receive(packet_receive);
		  }
		  catch (Exception x)
		  {
		    //System.out.println(x);
			if((System.currentTimeMillis()-start_time)>=(60000*MINUTES))
			  break;
		  }  	
	      receive_time=System.currentTimeMillis();
	      
	      String message = new String(rxbuffer,0,rxbuffer.length);
          //System.out.println(message);
	      //CHECK IF THE INCOMING PACKET IS CORRECT 
	      if(message.substring(0,5).equals("41 11"))
	      {
	    	  //CALCULATING FORMULA AND SAVE IT TO A FILE
	    	  FORMULA=hex2decimal(message.substring(6,8))*100/255;   
		      throttle_position_time.println(Long.toString(receive_time-start_time));
		      throttle_position_file.println(FORMULA);  
	      }       
          end_time=System.currentTimeMillis();
		}
		throttle_position_file.close();
		throttle_position_time.close();
		Socket_Send.close();
		Socket_Receive.close();
		System.out.println("----------------------------------------------------------");
	    System.out.println("End of Function : get_vehicle_OBD_II_throttle_position    ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_vehicle_OBD_II_speed(String vehicle_OBD_II_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		int MINUTES=5;
		System.out.println("----------------------------------------------------------");
	    System.out.println("       Function : get_vehicle_OBD_II_speed                ");
	    System.out.println("----------------------------------------------------------");
	    System.out.println("This procedure will take "+MINUTES +" minutes");
	    
	    String speed_code="01 0D";	   
	    long start_time,end_time,receive_time;
	    float FORMULA;
	    
	    vehicle_OBD_II_code+=speed_code;
		byte[] vehicle = vehicle_OBD_II_code.getBytes();
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(vehicle,vehicle.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(4000);
		
		byte[] rxbuffer = new byte[8];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		PrintStream speed_file = new PrintStream(new FileOutputStream("vehicle_speed.txt"));
        PrintStream speed_time = new PrintStream(new FileOutputStream("vehicle_speed_time.txt"));
		
		start_time=System.currentTimeMillis();
		end_time=0;
		while(end_time-start_time<60000*MINUTES)
		{
		  Socket_Send.send(packet_send);
		  try
		  {
		    Socket_Receive.receive(packet_receive);
		  }
		  catch (Exception x)
		  {
		    //System.out.println(x);
			if((System.currentTimeMillis()-start_time)>=(60000*MINUTES))
			  break;
		  }  	
	      receive_time=System.currentTimeMillis();
	      
	      String message = new String(rxbuffer,0,rxbuffer.length);
          //System.out.println(message);
	      //CHECK IF INCOMING PACKET IS CORRECT
	      if(message.substring(0,5).equals("41 0D"))
	      {
	    	  //CALCULATING FORMULA AND SAVE IT TO A FILE
	    	  FORMULA = hex2decimal(message.substring(6,8));
		      speed_time.println(Long.toString(receive_time-start_time));
		      speed_file.println(FORMULA);  
	      }     
          end_time=System.currentTimeMillis();
		}
		speed_file.close();
		speed_time.close();
		Socket_Send.close();
		Socket_Receive.close();
		System.out.println("----------------------------------------------------------");
	    System.out.println("       End of Function : get_vehicle_OBD_II_speed         ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_vehicle_OBD_II_coolant_temperature(String vehicle_OBD_II_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		int MINUTES=5;
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : get_vehicle_OBD_II_coolant_temperature      ");
	    System.out.println("----------------------------------------------------------");
	    System.out.println("This procedure will take "+MINUTES +" minutes");
	    
	    String coolant_temperature_code="01 05";	   
	    long start_time,end_time,receive_time;
	    float FORMULA;
	    
	    vehicle_OBD_II_code+=coolant_temperature_code;
		byte[] vehicle = vehicle_OBD_II_code.getBytes(); 
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(vehicle,vehicle.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(4000);
		
		byte[] rxbuffer = new byte[8];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		PrintStream coolant_temperature_file = new PrintStream(new FileOutputStream("coolant_temperature.txt"));
        PrintStream coolant_temperature_time = new PrintStream(new FileOutputStream("coolant_temperature_time.txt"));
		
		start_time=System.currentTimeMillis();
		end_time=0;
		while(end_time-start_time<60000*MINUTES)
		{
		  Socket_Send.send(packet_send);
		  try
		  {
		    Socket_Receive.receive(packet_receive);
		  }
		  catch (Exception x)
		  {
		    //System.out.println(x);
			if((System.currentTimeMillis()-start_time)>=(60000*MINUTES))
		      break;
		  }  	
	      receive_time=System.currentTimeMillis();
	      
	      String message = new String(rxbuffer,0,rxbuffer.length);
          //System.out.println(message);
	      //CHECKING IF INCOMING PACKET IS CORRECT
	      if(message.substring(0,5).equals("41 05"))
	      {
	    	  //CALCULATING FORMULA AND SAVE IT TO A FILE
	    	  FORMULA = hex2decimal(message.substring(6,8)) - 40;      
		      coolant_temperature_time.println(Long.toString(receive_time-start_time));
		      coolant_temperature_file.println(FORMULA);   
	      }          
          end_time=System.currentTimeMillis();
		}
		coolant_temperature_file.close();
		coolant_temperature_time.close();
		Socket_Send.close();
		Socket_Receive.close();
		System.out.println("----------------------------------------------------------");
	    System.out.println(" End of Function : get_vehicle_OBD_II_coolant_temperature ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_vehicle_OBD_II_engine_rpm(String vehicle_OBD_II_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException
	{
		int MINUTES=5;
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : get_vehicle_OBD_II_engine_rpm               ");
	    System.out.println("----------------------------------------------------------");
	    System.out.println("This procedure will take "+MINUTES +" minutes");
	    
	    String engine_rpm_code="01 0C";	   
	    long start_time,end_time,receive_time;
	    float FORMULA;
	    
	    vehicle_OBD_II_code+=engine_rpm_code;
		byte[] vehicle = vehicle_OBD_II_code.getBytes(); 
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(vehicle,vehicle.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(5000);
		
		byte[] rxbuffer = new byte[11];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		PrintStream engine_rpm_file = new PrintStream(new FileOutputStream("engine_rpm.txt"));
        PrintStream engine_rpm_time = new PrintStream(new FileOutputStream("engine_rpm_time.txt"));
		
		start_time=System.currentTimeMillis();
		end_time=0;
		while(end_time-start_time<60000*MINUTES)
		{
		  Socket_Send.send(packet_send);
		  try
		  {
		    Socket_Receive.receive(packet_receive);
		  }
		  catch (Exception x)
		  {
		    //System.out.println(x);
			if((System.currentTimeMillis()-start_time)>=(60000*MINUTES))
		      break;
		  }  	
	      receive_time=System.currentTimeMillis();
	      
	      String message = new String(rxbuffer,0,rxbuffer.length);
          //System.out.println(message);
	      //CHECK IF INCOMING PACKET IS CORRECT
          if(message.substring(0,5).equals("41 0C"))
          {
        	//CALCULATING FORMULA AND SAVE IT TO A FILE
            FORMULA = ((hex2decimal(message.substring(6,8))*256)+(hex2decimal(message.substring(9,11))))/4;
    	    engine_rpm_file.println(FORMULA);         
    	    engine_rpm_time.println(Long.toString(receive_time-start_time));
          }	      
          end_time=System.currentTimeMillis();
		}	
		engine_rpm_file.close();
		engine_rpm_time.close();
		Socket_Send.close();
		Socket_Receive.close();
		System.out.println("----------------------------------------------------------");
	    System.out.println(" End of Function : get_vehicle_OBD_II_engine_rpm          ");
	    System.out.println("----------------------------------------------------------");
	}
	
	public static int hex2decimal(String s)
    {
		     //CONVERT A HEX TO DECIMAL
             String digits = "0123456789ABCDEF";
             s = s.toUpperCase();
             int val = 0;
             for (int i = 0; i < s.length(); i++)
             { 
                 char c = s.charAt(i);
                 int d = digits.indexOf(c);
                 val = 16*val + d;
             }
             return val;
    }
	
	public static void get_audio_DPCM(String audio_request_code,int ServerPort,int ClientPort,byte[] hostIP) throws IOException, LineUnavailableException
	{ 		
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : get_audio_DPCM                         ");
	    System.out.println("----------------------------------------------------------");
	    
		byte[] audio = audio_request_code.getBytes();
		//WE SAVE HERE THE NUMBER OF PACKETS I WANT TO RECEIVE
		int sound_packets=Integer.parseInt(audio_request_code.substring(audio_request_code.length()-3, audio_request_code.length()));
		//System.out.println(sound_packets);
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(audio,audio.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(10000);
		
		byte[][] samples = new byte[sound_packets][256];
			
		int samples_col;
		//NIBBLES
		int nibble1,nibble2;
		
		byte[] rxbuffer = new byte[128];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		Socket_Send.send(packet_send);
		samples_col=0;
		for(int i=0;i<sound_packets;i++)
		{		
		  try
		  {
		    Socket_Receive.receive(packet_receive); 
		  }
		  catch(Exception x)
		  {
		    //System.out.println(x);
		    break;
		  }  
		  samples_col=0;
		  for(int j=0;j<rxbuffer.length;j++)
		  {
			  //GETTING THE FIRST NIBBLE
			  nibble1= (rxbuffer[j] & 0b11110000);
			  //SHIFTING IT 4 BITS RIGHT
			  nibble1= (nibble1 >> 4);
			  //GETTING THE SECOND NIBBLE
			  nibble2= (rxbuffer[j] & 0b00001111);
			  samples[i][samples_col]=(byte) nibble1;
			  samples[i][samples_col+1]=(byte) nibble2;
			  samples_col+=2;
		  }  
		}	    
		int x_previous=0;
		for(int i=0;i<sound_packets;i++)
		{
			for(int j=0;j<256;j++)
			{
				samples[i][j] = (byte) (x_previous + (int)samples[i][j] -8);
				x_previous=(int)samples[i][j];
				//System.out.print(array[i][j]+"  ");
			}
			//System.out.println();
		}
		Socket_Send.close();
		Socket_Receive.close();
		long start_time;
		System.out.println("Playing Music Now");
		AudioFormat linearPCM = new AudioFormat(8192,8,1,true,false);
		SourceDataLine lineOut = AudioSystem.getSourceDataLine(linearPCM);
		lineOut.open(linearPCM,sound_packets*256);
		lineOut.start();
		byte[] audioBufferOut = new byte[sound_packets*256];
		for(int i=0;i<sound_packets;i++)
		{
			for(int j=0;j<256;j++)
			{
				audioBufferOut[i*256+j]=samples[i][j];
			}
		}
		start_time = System.currentTimeMillis();
		lineOut.write(audioBufferOut,0,sound_packets*256);
		lineOut.stop();
		lineOut.close();
		System.out.println("End Of Music");
		System.out.println("Playing time : "+(float)(System.currentTimeMillis()-start_time)/1000);
		//DIFFERENCES AND SAMPLES
		//ARE SAVE TO TXT FILES
		PrintStream audio_clip_differences = new PrintStream(new FileOutputStream("audio_clip_DPCM_differences.txt"));
		PrintStream audio_clip_samples = new PrintStream(new FileOutputStream("audio_clip_DPCM_samples.txt"));
		audio_clip_differences.println((int)audioBufferOut[0]);
		audio_clip_samples.println((int)audioBufferOut[0]);
		for(int i=1;i<audioBufferOut.length;i++)
		{
		  audio_clip_differences.println((int)audioBufferOut[i]-(int)audioBufferOut[i-1]);
		  audio_clip_samples.println((int)audioBufferOut[i]);
		}
		audio_clip_differences.close();
		audio_clip_samples.close();
		
		System.out.println("----------------------------------------------------------");
	    System.out.println(" End of Function : get_audio_DPCM                    ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	public static void get_audio_clip_AQ_DPCM(String audio_request_code_clip,int ServerPort,int ClientPort,byte[] hostIP) throws IOException, LineUnavailableException
	{ 		
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : get_audio_clip_AQ_DPCM                       ");
	    System.out.println("----------------------------------------------------------");
	    
		byte[] audio = audio_request_code_clip.getBytes();
		int sound_packets=Integer.parseInt(audio_request_code_clip.substring(audio_request_code_clip.length()-3, audio_request_code_clip.length()));
		//System.out.println(sound_packets);
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(audio,audio.length,hostAddress,ServerPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ClientPort);
		Socket_Receive.setSoTimeout(15000);
		
		byte[] rxbuffer = new byte[132];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		int[] mean = new int[sound_packets];
		int[] beta = new int[sound_packets];
		byte[] msb_lsb = new byte[2];
		byte[][] incoming_packets = new byte[sound_packets][128];
		int[][] samples_as_int = new int[sound_packets][256];
		byte[][] samples_as_byte = new byte[sound_packets][512];
		int columns;
		int nibble1,nibble2;
		
		Socket_Send.send(packet_send);
		for(int i=0;i<sound_packets;i++)
		{		
		  try
		  {
		    Socket_Receive.receive(packet_receive); 
		  }
		  catch(Exception x)
		  {
		    //System.out.println(x);
		    break;
		  }  
		  //SAVE THE MEAN VALUE AND MAKE IT INTEGER
		  msb_lsb[0]=rxbuffer[1];
		  msb_lsb[1]=rxbuffer[0];
		  mean[i] = signedIntFromByte(msb_lsb);
		  //SAVE THE STEP AND MAKE IT INTEGER
		  msb_lsb[0]=rxbuffer[3];
		  msb_lsb[1]=rxbuffer[2];
		  beta[i] = unsignedIntFromByte(msb_lsb);
		  //SAVING EACH INCOMING PACKET TO THE 2D BYTE ARRAY
		  for(int j=4;j<rxbuffer.length;j++)
		  {
			  incoming_packets[i][j-4]=rxbuffer[j];
		  }	  
		}
		Socket_Send.close();
		Socket_Receive.close();
		
		//EXTRACT HE FIRST AND THE SECOND NIBBLE AND SAVE THEM TO A NEW INT 2D ARRAY
		columns = 0;
		for(int i=0;i<sound_packets;i++)
		{
			for(int j=0;j<128;j++)
			{
				nibble1 = (int)(((int)incoming_packets[i][j] & (int)0b11110000) >> 4);
				nibble2 = ((int)incoming_packets[i][j] & (int)0b00001111);
				samples_as_int[i][columns] = nibble1;
				samples_as_int[i][columns+1] = nibble2;
				columns+=2;
			}
			columns = 0;
		}
		
		//USING THE FORMULA OF DECODING
		int x_previous = 0;
		for(int i=0;i<sound_packets;i++)
		{
			for(int j=0;j<256;j++)
			{
			  samples_as_int[i][j] = samples_as_int[i][j]*beta[i] + x_previous - 8*beta[i];
			  x_previous = samples_as_int[i][j];
			}
		}
		//SAVE THE INT 16 BIT SAMPLES TO THE 2D BYTE ARRAY WITH THE DOUBLE SIZE
		columns = 0;
		byte[] byteFromInt = new byte[4];
		for(int i=0;i<sound_packets;i++)
		{
			for(int j=0;j<256;j++)
			{
				byteFromInt = toBytes(samples_as_int[i][j]);
				samples_as_byte[i][columns] = byteFromInt[2];
				samples_as_byte[i][columns+1] = byteFromInt[3];
				columns+=2;
			}
			columns = 0;
		}
		
		//PLAY THE AUDIO
		long start_time;
		System.out.println("Playing Music Now");
		AudioFormat linearPCM = new AudioFormat(8192,16,1,true,true);
		SourceDataLine lineOut = AudioSystem.getSourceDataLine(linearPCM);
		lineOut.open(linearPCM,sound_packets*512);
		lineOut.start();
		byte[] audioBufferOut = new byte[sound_packets*512];
		for(int i=0;i<sound_packets;i++)
		{
			for(int j=0;j<512;j++)
			{
				audioBufferOut[i*512+j]=samples_as_byte[i][j];
			}
		}
		start_time = System.currentTimeMillis();
		lineOut.write(audioBufferOut,0,sound_packets*512);
		lineOut.stop();
		lineOut.close();
		System.out.println("End Of Music");
		System.out.println("Playing time : "+(float)(System.currentTimeMillis()-start_time)/1000);
		
		
		//SAVING TO TEXT FILES THE MEAN VALUES AND THE STEP OF THE QUANTIZER
		//AND ALSO THE SAMPLES AND THE DIFFERENCES OF THE SONG
		PrintStream audio_clip_differences = new PrintStream(new FileOutputStream("AQ_DPCM_differences.txt"));
		PrintStream audio_clip_samples = new PrintStream(new FileOutputStream("AQ_DPCM_samples.txt"));
		PrintStream audio_clip_mi = new PrintStream(new FileOutputStream("AQ_DPCM_mi.txt"));
		PrintStream audio_clip_beta = new PrintStream(new FileOutputStream("AQ_DPCM_beta.txt"));
		
		x_previous = 0;
		for(int i=0;i<sound_packets;i++)
		{
			audio_clip_mi.println(mean[i]);
			audio_clip_beta.println(beta[i]);
			for(int j=0;j<256;j++)
			{
				audio_clip_samples.println(samples_as_int[i][j]);
				audio_clip_differences.println(samples_as_int[i][j]-x_previous);
				x_previous = samples_as_int[i][j]; 
			}
		}
		audio_clip_differences.close();
		audio_clip_samples.close();
		audio_clip_mi.close();
		audio_clip_beta.close();
		
		
		
		System.out.println("----------------------------------------------------------");
	    System.out.println(" End of Function : get_audio_clip_AQ_DPCM                 ");
	    System.out.println("----------------------------------------------------------");
	}
	
	
	//TURN INTEGERS TO BYTE ARRAY
	public static byte[] toBytes(int i)
	{
	  byte[] result = new byte[4];
	  result[0] = (byte) (i >> 24);
	  result[1] = (byte) (i >> 16);
	  result[2] = (byte) (i >> 8);
	  result[3] = (byte) (i );
	  return result;
	}
	
	//CONVERT A BYTE ARRAY INTO AN SIGNED INTEGER
	public static int signedIntFromByte(byte[] b)
    {
		ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
		int num = (int)wrapped.getShort(); // 1
		return num;
	}
	
	//CONVERT A BYTE ARRAY INTO AN UNSIGNED INTEGER
	public static int unsignedIntFromByte(byte[] b)
    {
	    int l = 0;
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    return l;
	}
	
	
	public static void ithaki_copter(String ithakicopter_code,byte[] hostIP) throws IOException
	{ 
		System.out.println("----------------------------------------------------------");
	    System.out.println("   Function : ithaki_copter                               ");
	    System.out.println("----------------------------------------------------------");
		byte[] ithaki_copter = ithakicopter_code.getBytes();
		
		//SENDING PACKETS TO 38048 AND RECEIVING TO 48038
		int SendPort=38048;
		int ReceivePort=48038;
		//USEFUL VARIABLES IN ORDER TO STOP THE ENDLESS LOOP
		int LMOTOR,RMOTOR;
		long start_time,receive_time;
		
		DatagramSocket Socket_Send = new DatagramSocket();
		InetAddress hostAddress = InetAddress.getByAddress(hostIP);
		
		DatagramPacket packet_send = new DatagramPacket(ithaki_copter,ithaki_copter.length,hostAddress,SendPort);
		DatagramSocket Socket_Receive = new DatagramSocket(ReceivePort);
		Socket_Receive.setSoTimeout(5000);
		
		//HERE WE WILL SAVE THE INCOMING PACKETS
		byte[] rxbuffer = new byte[113];
		DatagramPacket packet_receive = new DatagramPacket(rxbuffer,rxbuffer.length);
		
		//4 FILES
		/*
		 * ITHAKICOPTER_altitude.txt -> SAVE HOW ALTITUDE IS CHANGED
		 * ITHAKICOPTER_temperature.txt -> SAVE HOW TEMPERATURE IS CHANGED
		 * ITHAKICOPTER_pressure.txt -> SAVE HOW PRESSURE IS CHANGED
		 * ITHAKICOPTER_flight_time.txt -> SAVE THE TIME OF EACH SAMPLE
		 * 
		 */
		PrintStream ITHAKICOPTER_altitude = new PrintStream(new FileOutputStream("ITHAKICOPTER_altitude.txt"));
        PrintStream ITHAKICOPTER_temperature = new PrintStream(new FileOutputStream("ITHAKICOPTER_temperature.txt"));
        PrintStream ITHAKICOPTER_pressure = new PrintStream(new FileOutputStream("ITHAKICOPTER_pressure.txt"));
        PrintStream ITHAKICOPTER_flight_time = new PrintStream(new FileOutputStream("ITHAKICOPTER_flight_time.txt"));
        start_time=System.currentTimeMillis();
        //START THE ENDLESS LOOP
		for(;;)
		{
			Socket_Send.send(packet_send);
			try
			{
				Socket_Receive.receive(packet_receive);	
				receive_time=System.currentTimeMillis();
			}
	        catch(Exception x)
			{
	        	System.out.println(x); 
	        	break;
			}
	        String message = new String(rxbuffer,0,rxbuffer.length);
	        //System.out.println(message);
	        LMOTOR=Integer.parseInt(message.substring(40,43));
	        RMOTOR=Integer.parseInt(message.substring(51,54));
	        //STOP WHEN THE COPTER STOPS
	        if(LMOTOR==00&&RMOTOR==0)
	        {
	        	break;
	        }
	        //SAVE RESULTS TO EACH FILE
	        ITHAKICOPTER_altitude.println((message.substring(64,67)));
	        ITHAKICOPTER_temperature.println(message.substring(80,86));
	        ITHAKICOPTER_pressure.println(message.substring(96,103));
	        ITHAKICOPTER_flight_time.println(receive_time-start_time);	        
		}	
		ITHAKICOPTER_altitude.close();
		ITHAKICOPTER_temperature.close();
		ITHAKICOPTER_pressure.close();
		ITHAKICOPTER_flight_time.close();
		Socket_Send.close();
		Socket_Receive.close();
		
		System.out.println("----------------------------------------------------------");
	    System.out.println(" End of Function : ithaki_copter                          ");
	    System.out.println("----------------------------------------------------------");
	}
	
}
