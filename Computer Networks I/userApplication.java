/*
 *                      DIKTYA YPOLOGISTWN I
 *                
 *                  STUDENT : PIPERIDIS ANESTIS
 *                
 * 
 * 
 */
package ithaki;

import ithakimodem.Modem;
import java.io.*;
import java.util.ArrayList;

public class virtualModem 
{
	 public static void main(String[] param) throws IOException 
	 {
	   (new virtualModem()).demo();
	 }
	 
	 
	 public void demo() throws IOException  
	 {
	   //ECHO REQUEST CODE	 
	   String echo="E5007\r";
	   //IMAGE ERROR FREE REQUEST CODE
	   String image_error_free="M2081\r";
	   //IMAGE WITH ERROR REQUEST CODE
	   String image_with_error="G2397\r";
	   //GPS REQUEST CODE
	   String gps="P2232\r";
	   //CODE FOR THE LIST OF 99 PLACES STARTING FROM 0100 (SESSION 1)
	   String gps_places_session1="R=1010099\r";
	   //CODE FOR THE LIST OF 99 PLACES STARTING FROM 0200 (SESSION 2)
	   String gps_places_session2="R=1020099\r";
	   //ACK REQUEST CODE
	   String ack="Q1602\r";
	   //NACK REQUEST CODE
	   String nack="R2984\r";

	   //STARTING COMMUNICATION WITH ITHAKI AND PRINTING THE WELCOME MESSAGE
	   int k=0;
	   Modem modem;
	   modem=new Modem();
	   modem.setSpeed(40000);
	   modem.setTimeout(600);
	   modem.open("ITHAKI");
		 for (;;) 
		 {
		   try 
		   {
		     k=modem.read();
		     if (k==-1) break;
		     System.out.print((char)k);    
		   } catch (Exception x) 
		   {
		     break;
		   }
		 }
	   
	   /*      FUNCTIONS FOR RECEIVING PACKETS
	    * EACH FUNCTION REFERS TO A SPECIFIC REQUEST CODE
	    * NOTE:USE ONE FUNCTION AT A TIME
	    * WHICH FUNCTION YOU WANT JUST REMOVE THE // FROM COMMENT
	    */
		 
	   //receive_echo_packets(modem,echo,k);
	   //receive_image_no_error(modem,image_error_free,k);
	   //receive_image_with_error(modem,image_with_error,k);
	   //receive_gps_packet(modem,(gps.substring(0,5)+gps_places_session2),k);
	   //receive_ack_nack_packets(modem,ack,nack,k);
	   
	   modem.close();
       //END OF COMMUNICATION WITH ITHAKI
	   System.out.println();
	   System.out.println("END OF PROGRAM");
}
	 
	 
	 //FUNCTION FOR RECEIVING ECHO PACKETS 
	 //SENDING ECHO REQUEST CODE TO ITHAKI
	 
	 public void receive_echo_packets(Modem modem,String echo,int k) throws IOException
	 {
		 int MINUTES=30;
		 System.out.println("------------------------------------------------------------------");
		 System.out.println("             FUNCTION: receive_echo_packets                        ");
		 System.out.println();
		 System.out.println("This procedure will take "+MINUTES +" minutes");
		 
		 /*  WE CREATE TWO FILES
		  * 
		  * echo_packets.txt IS A FILE IN WHICH WE SAVE THE PACKETS ITHAKI SENDS TO OUR COMPUTER
		  * 
		  * echo_packets_responses_in_milliseconds.txt IS A FILE IN WHICH WE SAVE THE RESPONSE IN
		  * MILLISECONDS
		  * 
		  */
		 PrintStream echo_file = new PrintStream(new FileOutputStream("echo_packets.txt"));
		 PrintStream echo_file_responses = new PrintStream(new FileOutputStream("echo_packets_responses_in_milliseconds.txt"));	 
  	     /*  START_TIME AND END_TIME ARE USEFUL FOR COUNTING TIME FOR THE N-MINUTE PROCEDURE
  	     *   OF SENDING PACKETS TO ITHAKI
  	     *   SEND_TIME IS THE TIME WHEN WE SEND THE PACKET TO ITHAKI 
  	     *   RECEIVE_TIME IS THE TIME WHEN WE RECEIVE ALL THE ECHO PACKET FROM ITHAKI
  	     *   RECEIVE_TIME - SEND_TIME IS THE RESPONSE TIME  
  	     */
  	     long start_time,end_time,send_time,receive_time;
  	     receive_time=0;
  	     // STARTING PROCEDURE OF SENDING PACKETS  
  	     String echo_packet=""; 
  	     start_time=System.currentTimeMillis();  
  	     for(;;)
  	     {   
  	       //SAVE THE SYSTEM'S TIME IN MILIISECONDS
  	       send_time=System.currentTimeMillis();
    	   modem.write(echo.getBytes());	
    	   //START READING THE PACKET
    	   for (;;) 
	       {
	         try 
	         {
	           k=modem.read();
	           //IF THE TRANSMITION IS NOT FINISHED
	           if(k!=-1)
	           echo_packet+=(char)k;
	           //IF IT IS THE END OF THE PACKET TRANSMITION
	           if (k==-1) 
	           {   
	        	   //SAVE THE TIME ( IT IS THE RESPONSE TIME)
	               receive_time=System.currentTimeMillis();	      	            
	        	   break;
	           }
	         } catch (Exception x) 
	         {
	           break;
	         }   
	       }
    	   //CHECKING IF THE PACKET IS THE EXPECTED ONE
    	   //IF THE LENGTH IS THE EXPECTED ONE 
    	   //THEN CHECK IF THE PACKET STARTS WITH PSTART AND ENDS WITH PSTOP
    	   //IF THE PACKET IS NOT THE EXPECTED ONE DO NOTHING
           if(echo_packet.length()==35)
           {
        	   if((echo_packet.substring(0,6).equals("PSTART"))&&(echo_packet.substring(30,35).equals("PSTOP")))
        	   {
        		   //IF THE PACKET IS THE EXPECTED ONE THEN SAVE THE PACKET AND THE
        		   //RESPONSE TIME TO FILES
        		   echo_file_responses.print(Long.toString(receive_time-send_time));
	               echo_file_responses.print("\n"); 
	               echo_file.print(echo_packet);
	    	       echo_file.print("\n");
        	   }
           }       
           //PREPARING FOR THE NEXT INCOMING PACKET
    	   echo_packet="";
  	       end_time=System.currentTimeMillis();
  	       //CHECK IF THE N-MINUTE PROCEDURE IS FINISHED
  	       if(end_time-start_time>=60000*MINUTES)
  	    	   break;
  	     } 
  	     //CLOSE ALL FILES WE OPENED
  	     echo_file_responses.close();
  	     echo_file.close();
  	     
  	     System.out.println();
  	     System.out.println("         Returning from FUNCTION: receive_echo_packets          ");
  	     System.out.println("------------------------------------------------------------------");
  	         
	 }
	 
	 //      FUNCTION FOR RECEIVING IMAGE WITHOUT ERRROR 
     //  SENDING IMAGE REQUEST CODE (TX/RX ERROR FREE) TO ITHAKI
	 public void receive_image_no_error(Modem modem,String image_error_free,int k) throws IOException
	 {
		 System.out.println("------------------------------------------------------------------");
		 System.out.println("             FUNCTION: receive_image_no_error                     ");
		 System.out.println();
		 
		 //CREATING AN ARRAYLIST IN WHICH WE WILL SAVE THE BYTES OF THE PACKET IN INTEGER MODE
		 ArrayList<Integer> image = new ArrayList<Integer>();
		 //NEXT TWO VARIABLES ARE VERY USEFUL IN CASE OF UNEXPECTED ERRORS
		 // flag SHOWS US IF WE RECEIVE PACKET OF MORE THAN 4 BYTES SO I CAN SEARCH FOR THE DELIMITERS
		 boolean flag=false;
		 // tries SHOWS US THAT IF WE FAIL TAKING A PACKET 4 TIMES THEN EXIT
		 int tries=0;
		 while(flag==false)
	     {
			 image.clear();
			 //SENDING THE REQUEST CODE
	  	     modem.write(image_error_free.getBytes());
	  	     //READING THE INCOMING PACKET
	  	     for (;;) 
	  	     {
	  	       try 
	  	       {
	  	         k=modem.read();
	  	    	 if((k!=-1))
	  	    	 {
	  	    		image.add(k);   		 
	  	    	 }
	  	         if (k==-1) break;       
	  	       } catch (Exception x) 
	  	       {
	  	         break;
	  	       }
	  	     }
	  	     if(image.size()<=4)
	  	     {
	  	    	 System.out.println("An unexpected ERROR happened");
		    	 System.out.println("We are sending the request code "+image_error_free.substring(0,5)+" again to ITHAKI");
		    	 tries++;
	  	     }
	  	     else
	  	    	 flag=true;
	  	     //IF WE FAILED 4 TIMES TO RECEIVE SOMETHING MORE THAN 4 BYTES RETURN TO MAIN
	  	     if(tries==4)
	  	     {
	  	    	System.out.println("Tried too many times to receive packet");
	  	    	System.out.println("Exit function: receive_image_no_error");
	  	    	return ;
	  	     }	  	    	 
	     }
  	     //CHECKING IF THE FIRST BYTE=0XFF , SECOND BYTE = 0XD8, SEMI LAST BYTE=0XFF
  	     //AND LAST BYTE = 0XD9
  	     //IN INTEGER MODE THESE BYTES HAVE VALUES 255,216,255,217
  	     if((image.get(0)==255)&&(image.get(1)==216)&&(image.get(image.size()-2)==255)&&(image.get(image.size()-1)==217))
  	     {
  	    	 //AFTER I CHECKED THE DELIMITERS OF THE FILE NOW I AM SURE THAT THIS FILE IS AN IMAGE
  	    	 //CREATING THE FILE.JPG IN ORDER TO SAVE THE IMAGE
  		     FileOutputStream image_without_error = null;
  	         image_without_error = new FileOutputStream("image_error_free.jpg"); 
  	    	 for(int i=0;i<image.size();i++)
  	    	 {
  	    		image_without_error.write(image.get(i));
  	    	 }
  	    	 //CLOSE THE FILE
  	  	     image_without_error.close();
  	  	     System.out.println("Image with request code "+image_error_free.substring(0,5)+" successfully received");
  	  	     System.out.println("Size = "+image.size()+" Bytes");
  	     }
  	     //IF THE DELIMITERS ARE NOT THE RIGHT JUST DO NOTHING
  	     else
	     {
	    	 System.out.println("Cannot download The image because the delimiters are not the expected");
	     }
  	     
  	     System.out.println();
	     System.out.println("         Returning from FUNCTION: receive_image_no_error          ");
	     System.out.println("------------------------------------------------------------------");
	 }
	 
     //   FUNCTION FOR RECEIVING IMAGE WITH ERRROR 
     //  SENDING IMAGE REQUEST CODE (TX/RX WITH ERROR ) TO ITHAKI
	 public void receive_image_with_error(Modem modem,String image_with_error,int k) throws IOException
	 {   
		 System.out.println("------------------------------------------------------------------");
		 System.out.println("             FUNCTION: receive_image_with_error                   ");
		 System.out.println();
		 
		 //CREATING AN ARRAYLIST IN WHICH WE WILL SAVE THE BYTES OF THE PACKET IN INTEGER MODE
		 ArrayList<Integer> image = new ArrayList<Integer>();
		 //NEXT TWO VARIABLES ARE VERY USEFUL IN CASE OF UNEXPECTED ERRORS
		 // flag SHOWS US IF WE RECEIVE PACKET OF MORE THAN 4 BYTES SO I CAN SEARCH FOR THE DELIMITERS
		 boolean flag=false;
		 // tries SHOWS US THAT IF WE FAIL TAKING A PACKET 4 TIMES THEN EXIT
		 int tries=0;
		 while(flag==false)
	     {
			 image.clear();
			 //SENDING THE REQUEST CODE
	  	     modem.write(image_with_error.getBytes());
	  	     //READING THE INCOMING PACKET
	  	     for (;;) 
	  	     {
	  	       try 
	  	       {
	  	         k=modem.read();
	  	    	 if((k!=-1))
	  	    	 {
	  	    		image.add(k);   		 
	  	    	 }
	  	         if (k==-1) break;       
	  	       } catch (Exception x) 
	  	       {
	  	         break;
	  	       }
	  	     }
	  	     if(image.size()<=4)
		     {
		    	 System.out.println("An unexpected ERROR happened");
		    	 System.out.println("We are sending the request code "+image_with_error.substring(0,5)+" again to ITHAKI");
		    	 tries++;
		     }
		     else
		    	 flag=true;
	  	     //IF WE FAILED 4 TIMES TO RECEIVE SOMETHING MORE THAN 4 BYTES RETURN TO MAIN
	  	     if(tries==4)
	  	     {
	  	    	System.out.println("Tried too many times to receive packet");
	  	    	System.out.println("Exit function: receive_image_with_error");
	  	    	return ;
	  	     }	  
	  	    	 
	     }
  	     //CHECKING IF THE FIRST BYTE=0XFF , SECOND BYTE = 0XD8, SEMI LAST BYTE=0XFF
  	     //AND LAST BYTE = 0XD9
  	     //IN INTEGER MODE THESE BYTES HAVE VALUES 255,216,255,217 
  	     if((image.get(0)==255)&&(image.get(1)==216)&&(image.get(image.size()-2)==255)&&(image.get(image.size()-1)==217))
  	     {
  	    	 //AFTER I CHECKED THE DELIMITERS OF THE FILE NOW I AM SURE THAT THIS FILE IS AN IMAGE
  	    	 //CREATING THE FILE.JPG IN ORDER TO SAVE THE IMAGE
  		     FileOutputStream wrong_image = null;
  		     wrong_image = new FileOutputStream("image_with_error.jpg"); 
  	    	 for(int i=0;i<image.size();i++)
  	    	 {
  	    		wrong_image.write(image.get(i));
  	    	 }
  	    	 //CLOSE THE FILE
  	    	wrong_image.close();
  	    	System.out.println("Image with request code "+image_with_error.substring(0,5)+" successfully received");
  	    	System.out.println("Size = "+image.size()+" Bytes");
  	     }
  	     //IF THE DELIMITERS ARE NOT THE RIGHT JUST DO NOTHING
  	     else
  	     {
  	    	 System.out.println("Cannot download The image because the delimiters are not the expected");
  	     }
  	     
  	     System.out.println();
	     System.out.println("         Returning from FUNCTION: receive_image_with_error        ");
	     System.out.println("------------------------------------------------------------------");
	 }
	 
	 
	 //  FUNCTION FOR RECEIVING THE GPS PACKET AND THE IMAGE WITH 5 POSITIONS ON IT (FROM GOOGLE MAPS)
	 // SENDING THE GPS REQUEST CODE 
	 public void receive_gps_packet(Modem modem,String gps,int k) throws IOException
	 {
		 System.out.println("------------------------------------------------------------------");
		 System.out.println("             FUNCTION: receive_gps_packet                         ");
		 System.out.println();
		 
		 //WE WILL COUNT HOW MANY LINES THE FILES WILL HAVE
		 int line_counter=0;
		 String GPS_PACKET="";
         // SENDING THE REQUEST CODE               
  	     modem.write(gps.getBytes());
  	     //RECEIVING AND SAVING THE PACKET TO THE FILE
  	     for (;;) 
  	     {
  	       try 
  	       {
  	         k=modem.read();
  	         if(k!=-1)
  	        	GPS_PACKET+=(char)k;
  	         if (k==-1) break;    
  	       } catch (Exception x) 
  	       {
  	         break;
  	       }
  	     }    

  	     //COUNTING HOW MANY LINES WILL MY FILE BE
  	     for(int i=0;i<GPS_PACKET.length();i++)
  	        if(String.valueOf(GPS_PACKET.charAt(i)).equals("\n"))
  	    	  line_counter++;
  	     //WE TAKE 5 SPOTS WITH 10 SECONDS DIFFERENCE EACH
  	     //WHICH MEANS THAT WE WILL NEED AT LEAST 40 SPOTS IN THAT FILE
  	     //IF THE LINES ARE AT LEAST 42 ( INCLUDING THE START AND STOP GPS TRACKING )
  	     //WHICH MEANS THAT WE CAN TAKE 5 SPOTS
  	     //THEN TRY TO SEND THE IMAGE REQUEST CODE AND DOWNLOAD THE IMAGE
  	     if(line_counter>=42)
  	     {
  	    	 //CHECK THE START AND THE END OF THE FILE
  	    	 if((GPS_PACKET.substring(0,27).equals("START ITHAKI GPS TRACKING\r\n"))
  	     	        &&(GPS_PACKET.substring(GPS_PACKET.length()-26,GPS_PACKET.length()).equals("STOP ITHAKI GPS TRACKING\r\n")))
  	     	     {
  	     	    	 //CREATING THE FILE FOR SAVING THE LIST OF POSITIONS
  	     			 PrintStream gps_packet = new PrintStream(new FileOutputStream("gps_packet.txt"));
  	     	    	 gps_packet.print(GPS_PACKET);
  	     	  	     //CLOSE THE FILE
  	     	  	     gps_packet.close();
  	     	  	     
  	     	  	     //OPEN THE SAME FILE AS AN INPUT_STREAM THIS TIME
  	     	  	     File fin = new File("gps_packet.txt");
  	     	  	     FileInputStream fis = new FileInputStream(fin);
  	     	  	 
  	     	  	     //Construct BufferedReader from InputStreamReader
  	     	  	     BufferedReader br = new BufferedReader(new InputStreamReader(fis));
  	     	   
  	     	  	     String line=null;
  	     	  	     String T="T=";
  	     	  	     String slash="\r";
  	     	  	     String image_request;
  	     	  	     // STRING AABBCCDDEEFF_i REFERS TO EACH OF THE 5 PLACES ON THE IMAGE
  	     	  	     String AABBCCDDEEFF_1,AABBCCDDEEFF_2,AABBCCDDEEFF_3,AABBCCDDEEFF_4,AABBCCDDEEFF_5;
  	     	  	     AABBCCDDEEFF_1=AABBCCDDEEFF_2=AABBCCDDEEFF_3=AABBCCDDEEFF_4=AABBCCDDEEFF_5=null;

  	     	  	     /* START READING THE LINES FROM THE FILE  gps_packet.txt
  	     	  	     * WE WILL ONLY NEED THE LINES 2,12,22,32,42
  	     	  	      * 
  	     	  	      */	     	  	    
  	     	  	     line_counter=0;
  	     	  	     for(;;)
  	     	  	     {
  	     	  		   line = br.readLine();
  	     	  		   //IF THIS IS LINE 2 THEN SAVE IT
  	     	  		   if(line_counter==1)
  	     	  		   {
  	     	  			 //System.out.println(line);
  	     	  			 /*
  	     	  			  * FIRST LINE OF THE NEXT COMMAND IS GETTING THE AABB
  	     	  			  * SECOND LINE IS FOR GETTING CC AND MAKING IT INTO SECONDS
  	     	  			  * THIRD LINE IS FOR GETTING DDEE
  	     	  			  * FOURTH LINE IS FOR GETTING FF AND MAKING IT SECONDS
  	     	  			  * SO THIS WAY WE TAKE THE CODE AABBCCDDEEFF ITHAKI NEEDS
  	     	  			  */
  	     	  		
  	     	             AABBCCDDEEFF_1=line.substring(31,35)
  	     	  		  	       +String.valueOf((int)((new Float(line.substring(35,40)).floatValue())*60))
  	     	  		  	       +line.substring(18,22)
  	     	  		  	       +String.valueOf((int)((new Float(line.substring(22,27)).floatValue())*60));
  	     	  		   }
  	     	  		   //IF THIS IS LINE 12 THEN SAVE IT
  	     	  		   else if(line_counter==11)
  	     	  		   {
  	     	  			 //System.out.println(line);
  	     	  			 AABBCCDDEEFF_2=line.substring(31,35)
  	     	  		  	       +String.valueOf((int)((new Float(line.substring(35,40)).floatValue())*60))
  	     	  		  	       +line.substring(18,22)
  	     	  		  	       +String.valueOf((int)((new Float(line.substring(22,27)).floatValue())*60));
  	     	  		   }
  	     	  		   //IF THIS IS LINE 22 THEN SAVE IT
  	     	  		   else if(line_counter==21)
  	     	  		   {
  	     	  			 //System.out.println(line);
  	     	  			 AABBCCDDEEFF_3=line.substring(31,35)
  	     	  		  	       +String.valueOf((int)((new Float(line.substring(35,40)).floatValue())*60))
  	     	  		  	  	   +line.substring(18,22)
  	     	  		  	  	   +String.valueOf((int)((new Float(line.substring(22,27)).floatValue())*60));
  	     	  		   }
  	     	  		   //IF THIS IS LINE 32 THEN SAVE IT
  	     	  		   else if(line_counter==31)
  	     	  		   {
  	     	  			 //System.out.println(line);
  	     	  			 AABBCCDDEEFF_4=line.substring(31,35)
  	     	  		  	  	   +String.valueOf((int)((new Float(line.substring(35,40)).floatValue())*60))
  	     	  		  	  	   +line.substring(18,22)
  	     	  		  	  	   +String.valueOf((int)((new Float(line.substring(22,27)).floatValue())*60));
  	     	  		   }
  	     	  		  //IF THIS IS LINE 42 THEN SAVE IT
  	     	  		   else if(line_counter==41)
  	     	  		   {
  	     	  			 //System.out.println(line);
  	     	  			 AABBCCDDEEFF_5=line.substring(31,35)
  	     	  	  	  	  	   +String.valueOf((int)((new Float(line.substring(35,40)).floatValue())*60))
  	     	  	  	  	  	   +line.substring(18,22)
  	     	  	  	  	  	   +String.valueOf((int)((new Float(line.substring(22,27)).floatValue())*60));
  	     	  		   }
  	     	  		   //PREPARING FOR THE NEXT LINE
  	     	  		   line_counter++;
  	     	  		   //WHEN YOU SAVED ALL THE 5 ABOVED PLACES THEN STOP SEARCHING
  	     	  		   if(line_counter==42)
  	     	  			break;
  	     	  	     }
  	     	  	     //CLOSE THE FILE
  	     	  	     br.close();      	  	     	   
  	     	  	     //PREPARING THE CODE WE WILL SEND TO ITHAKI
  	     		     image_request=gps.substring(0,5)+T+AABBCCDDEEFF_1+T+AABBCCDDEEFF_2+T
  	     		    		+AABBCCDDEEFF_3+T+AABBCCDDEEFF_4+T+AABBCCDDEEFF_5+slash;
  	     		    		   
  	     		     System.out.println("image request code = "+image_request);
  	     		     
  	     		     //CREATING AN ARRAYLIST IN WHICH WE WILL SAVE THE BYTES OF THE PACKET IN INTEGER MODE
  	     			 ArrayList<Integer> image = new ArrayList<Integer>();
  	     			 //NEXT TWO VARIABLES ARE VERY USEFUL IN CASE OF UNEXPECTED ERRORS
  	     			 // flag SHOWS US IF WE RECEIVE PACKET OF MORE THAN 4 BYTES SO I CAN SEARCH FOR THE DELIMITERS
  	     			 boolean flag=false;
  	     			 // tries SHOWS US THAT IF WE FAIL TAKING A PACKET 4 TIMES THEN EXIT
  	     			 int tries=0;
  	     			 while(flag==false)
  	     		     {
  	     				 image.clear();
  	     				 //SENDING THE REQUEST CODE
  	     		  	     modem.write(image_request.getBytes());
  	     		  	     //READING THE INCOMING PACKET
  	     		  	     for (;;) 
  	     		  	     {
  	     		  	       try 
  	     		  	       {
  	     		  	         k=modem.read();
  	     		  	    	 if((k!=-1))
  	     		  	    	 {
  	     		  	    		image.add(k);   		 
  	     		  	    	 }
  	     		  	         if (k==-1) break;       
  	     		  	       } catch (Exception x) 
  	     		  	       {
  	     		  	         break;
  	     		  	       }
  	     		  	     }
  	     		  	     if(image.size()<=4)
  	     		  	     {
  	     		  	    	 System.out.println("An unexpected ERROR happened");
  	     			    	 System.out.println("We are sending the request code"+image_request+"again to ITHAKI");
  	     			    	 tries++;
  	     		  	     }
  	     		  	     else
  	     		  	    	 flag=true;
  	     		  	     //IF WE FAILED 4 TIMES TO RECEIVE SOMETHING MORE THAN 4 BYTES RETURN TO MAIN
  	     		  	     if(tries==4)
  	     		  	     {
  	     		  	    	System.out.println("Tried too many times to receive packet");
  	     		  	    	System.out.println("Exit function: receive_gps_packet");
  	     		  	    	return ;
  	     		  	     }	
  	     		     }
  	     	  	     //CHECKING IF THE FIRST BYTE=0XFF , SECOND BYTE = 0XD8, SEMI LAST BYTE=0XFF
  	     	  	     //AND LAST BYTE = 0XD9
  	     	  	     //IN INTEGER MODE THESE BYTES HAVE VALUES 255,216,255,217
  	     	  	     if((image.get(0)==255)&&(image.get(1)==216)&&(image.get(image.size()-2)==255)&&(image.get(image.size()-1)==217))
  	     	  	     {
  	     	  	    	 //AFTER I CHECKED THE DELIMITERS OF THE FILE NOW I AM SURE THAT THIS FILE IS AN IMAGE
  	     	  	    	 //CREATING THE FILE.JPG IN ORDER TO SAVE THE IMAGE
  	     	  		     FileOutputStream gps_image = null;
  	     	  		     gps_image = new FileOutputStream("gps_image.jpg"); 
  	     	  	    	 for(int i=0;i<image.size();i++)
  	     	  	    	 {
  	     	  	    	   gps_image.write(image.get(i));
  	     	  	    	 }
  	     	  	    	 //CLOSE THE FILE
  	     	  	         gps_image.close();
  	     	  	  	     System.out.println("Image with request code "+image_request+"successfully received");
  	     	  	  	     System.out.println("Size = "+image.size()+" Bytes");
  	     	  	     }
  	     	  	     //IF THE DELIMITERS ARE NOT THE RIGHT JUST DO NOTHING
  	     	  	     else
  	     		     {
  	     		    	 System.out.println("Cannot download The image because the delimiters are not the expected");
  	     		    	 System.out.println("exiting FUNCTION: receive_gps_packet"); 
  	     		    	 return ;
  	     		     }
  	     		     
  	     	     }
  	    	     //IF THE FILE's FISRT AND LAST LINE ARE NOT THE EXPECTED
  	     	     else
  	     	     {
  	     	    	 System.out.println("The file doesnt start with START ITHAKI GPS TRACKING");
  	     	    	 System.out.println("and doesnt end with STOP ITHAKI GPS TRACKING");
  	     	    	 System.out.println("exiting FUNCTION: receive_gps_packet"); 
  	     	    	 return ;
  	     	     } 	    	 
  	     }
  	     //IF THE FILE HAVE LESS THAN 42 LINES ( 40 SPOTS)
  	     else
  	     {
  	    	 System.out.println("We want to take 5 spots please change the number of spots of the file");
  	    	 System.out.println("exiting FUNCTION: receive_gps_packet"); 
  	    	 return ;
  	     }

  	     System.out.println();
	     System.out.println("         Returning from FUNCTION: receive_gps_packet              ");
	     System.out.println("------------------------------------------------------------------"); 	     
    }
	 
	 /*
	  * FUNCTION FOR GETTING PACKETS SENDING ACK AND NACK REQUEST CODES
	  */
	 public void receive_ack_nack_packets(Modem modem,String ack,String nack,int k) throws IOException
	 {
		 int MINUTES=30;
		 System.out.println("------------------------------------------------------------------");
		 System.out.println("             FUNCTION: receive_ack_nack_packets                   ");		 
		 System.out.println();
		 System.out.println("This procedure will take "+MINUTES +" minutes");
		 
		 /*
		  * OPENING 3 FILES 
		  * TO ack_nack_packets.txt WE WILL SAVE ONLY THE CORRECT PACKETS
		  * TO ack_nack_packets_responses_milliseconds.txt WE WILL SAVE THE TIME IT TOOK US TO TAKE A PACKET 
		  * SUCCESSFULLY
		  * TO ack_nack_packets_repeats.txt WE WILL SAVE HOW MANY TIMES WE SENT NACK CODE FOR EACH PACKET
		  */
		 PrintStream ack_nack_packets = new PrintStream(new FileOutputStream("ack_nack_packets.txt"));
		 PrintStream ack_nack_packets_responses = new PrintStream(new FileOutputStream("ack_nack_packets_responses_milliseconds.txt"));
		 PrintStream ack_nack_packets_repeats = new PrintStream(new FileOutputStream("ack_nack_packets_repeats.txt"));
		 
  	     //FLAG SHOWS US IF THE PACKET WE RECIEVED IS CORRECT OR NOT
  	     boolean flag=true;
  	     // repeats SHOW US HOW MANY TIMES I SENT NACK FOR EACH PACKET
  	     int repeats=0;
  	     long start_time,end_time,send_time,receive_time;
  	     send_time=0;   
  	     String packet_ack_nack="";
  	     start_time=System.currentTimeMillis();
  	     // VARIABLE total_packets IS USEFUL FOR COUNTING HOW MANY PACKETS WE RECEIVED
  	     // CORRECT AND INCORRECT
  	     int total_packets=0;
  	     for(;;)
  	     {
  	       //IF THE FLAG IS TRUE THIS MEANS THAT WE RECEIVED 
  	       // A CORRECT PACKET THEN WE WILL SEND THE ACK CODE
  		   if(flag)
  		   {
  		     repeats=0;
  		     send_time=System.currentTimeMillis();
  	         modem.write(ack.getBytes());
  		   }
  		   //ELSE WE WILL SEND THE NACK CODE
  		   else
  		   {
  		     repeats++;
  		     modem.write(nack.getBytes());
  		   }
  		   //READING THE PACKET
	       for (;;) 
	       {	 
	         try 
	         {
	           k=modem.read();
	           if(k!=-1)
	        	   packet_ack_nack+=(char)k;
	           if (k==-1) 
	           {
	        	 break;
	           }
	          } catch (Exception x) 
	          {
	            break;
	          }   
	        } 
	       //CHECKING IF THE PACKET IS THE EXPECTED AND THE CORRECT ONE
	       //IF THE PACKET HAS THE EXPECTED SIZE CONTINUE SEARCHING
	       if(packet_ack_nack.length()==58)
	       {
	    	   //IF THE PACKET STARTS WITH PSTART AND ENDS WITH PSTOP
	    	   //THE PACKET IS THE EXPECTED ONE
	    	   //AND NOW WE CHECK IF IT IS CORRECT
	    	   if((packet_ack_nack.substring(0,6).equals("PSTART"))&&(packet_ack_nack.substring(53,58).equals("PSTOP")))
	    	   {
	    		   //CALCULATING THE XOR OF 16 CHARACTERS AND COMPARE IT WITH THE FCS NUMBER
	    		   byte[] message = new byte[16]; 
	    	  	   message=packet_ack_nack.substring(31,47).getBytes();
	    	  	   int FCS = Integer.parseInt(packet_ack_nack.substring(49,52));
	    		   int XOR=message[0]^message[1];
	    		   for(int i=2;i<message.length;i++)
	    			   XOR=((byte)XOR)^(message[i]);
	    		   //IF IS CORRECT PACKET		  
	    		   //SEND ACK CODE
	    		   if(XOR==FCS)
	    		   {
	    			   receive_time=System.currentTimeMillis();
	    			   flag=true; 			   
	    			   ack_nack_packets.print(packet_ack_nack);
	    			   ack_nack_packets.print("\n");
	    			   ack_nack_packets_responses.print(Long.toString(receive_time-send_time));
	    			   ack_nack_packets_responses.print("\n");
	    			   ack_nack_packets_repeats.print(String.valueOf(repeats));
	    			   ack_nack_packets_repeats.print("\n");	    			   
	    		   }
	    		   //PACKET NOT CORRECT
	    		   //SEND NACK CODE
	    		   else
	    		   {
	    			   flag=false;
	    		   }		  		   
	    	   }
	    	   //PACKET IS NOT THE EXPECTED ONE
	    	   //SEND NACK CODE
	    	   else
	    	   {
	    		   flag=false;
	    	   }  	   
	       }
	       //PACKET IS NOT THE EXPECTED ONE
    	   //SEND NACK CODE
	       else
	       {
	         flag=false;
	       }
	       total_packets++;
	       //PREPARING FOR THE NEXT INCOMING PACKET
	       packet_ack_nack="";
	       end_time=System.currentTimeMillis();
	       if((end_time-start_time>=60000*MINUTES))
	         break;    
  	     }
  	     //System.out.println();
  	     //System.out.println(total_packets);
  	     
  	     //CLOSE THE FILES
  	     ack_nack_packets.close();
  	     ack_nack_packets_responses.close();
  	     ack_nack_packets_repeats.close();
  	     
  	     System.out.println();
	     System.out.println("         Returning from FUNCTION: receive_ack_nack_packets        ");
	     System.out.println("------------------------------------------------------------------"); 
	 }	
}
