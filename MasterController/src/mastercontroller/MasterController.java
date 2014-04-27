/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mastercontroller;


import java.net.*;
import java.io.*;
import java.lang.*;
import static java.lang.Thread.sleep;

public class MasterController {

    public static void main(String args[]) throws InterruptedException {

    	int TIME_MESSAGE_ID = 256;
    	int STATE_MESSAGE_ID = 304;
    	int TORQUE_MESSAGE_ID = 300;
    	int MOTOR_MESSAGE_ID = 296;
    	int CONTROL_LEVER_MESSAGE_ID = 641;
    	int TENSION_MESSAGE_ID = 448;
    	int CABLE_ANGLE_MESSAGE_ID = 464;
    	int LAUNCH_PARAM_MESSAGE_ID = 327;

    	byte currentTime = -1;

    	CanCnvt timeMessage = new CanCnvt();
    	timeMessage.id = TIME_MESSAGE_ID;
    	timeMessage.set_byte(0, 0); // time

    	float torqueScale = 1 / 32;
    	float torque = 150;

    	CanCnvt torqueMessage = new CanCnvt();
    	torqueMessage.id = TORQUE_MESSAGE_ID;
    	torqueMessage.set_short((short)(torque / torqueScale), 0); // torque
    	torqueMessage.set_byte(0, 2); // max cable speed
        
        int seq = 0;

        try {
            
	     //   ServerSocket providerSocket = new ServerSocket(32123);
	     //   System.out.println("Waiting for the connection");

	      //  Socket connection = providerSocket.accept();
            Socket connection = new Socket("127.0.0.1", 32123);
	        
	        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        OutputStreamWriter outstream = new OutputStreamWriter(connection.getOutputStream());

	        while (true) { // endless loop
	        	// advance the time
                    sleep(16);
	        	currentTime += 1;
                        timeMessage.seq = seq;
//                        timeMessage.dlc = 0;
                        timeMessage.set_byte(currentTime, 0); // time
	        	outstream.write(timeMessage.msg_prep());
                        seq += 1;
                        
                        torqueMessage.seq = seq;
                        torqueMessage.dlc = 2;
	        	outstream.write(torqueMessage.msg_prep());
	        	outstream.flush();
                        seq += 1;

	        	CanCnvt temp = new CanCnvt();

	        	System.out.println(timeMessage.msg_prep());
	        	System.out.println(torqueMessage.msg_prep());
	        }
        } catch(IOException e) {
        	e.printStackTrace();
        }


    }

}