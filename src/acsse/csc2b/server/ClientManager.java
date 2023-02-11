package acsse.csc2b.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClientManager 
{
    Socket connectionSocket = null;

    // byte streams
    OutputStream outputStream = null;
    InputStream inputStream = null;
    
    // binary streams (data)
    DataOutputStream dataOutputStream = null;
    BufferedOutputStream bufferedOutputStream = null;

    // text streams
    PrintWriter printWriter = null;
    BufferedReader bufferedReader = null;
    
    /** 
     * handles connection to server
     * @param portNumber port number to connect to
     */
    public void connect(int portNumber)
    {
        try 
        {
            connectionSocket = new Socket("localhost", portNumber);
            System.out.println("Connected to server on port 5000");

            // input
            inputStream = connectionSocket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            
            // output
            outputStream = connectionSocket.getOutputStream();
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            dataOutputStream = new DataOutputStream(bufferedOutputStream);
        } 
        catch(UnknownHostException e) 
        {
            e.printStackTrace();
        }
        catch(IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void processImage(String URL, File chosenFile, ImageView postImageView)
    {
        String encodedFile = null;	
		
		// convert file to an array of bytes
		byte[] bytes = new byte[(int)chosenFile.length()];
        FileInputStream fileInputStream = null;

        try 
        {
            fileInputStream = new FileInputStream(chosenFile);
            fileInputStream.read(bytes);
            encodedFile = new String(Base64.getEncoder().encodeToString(bytes));
            
            // send Bytes Across
            byte[] byteSend = encodedFile.getBytes();
            
            // HTTP POST Command
            dataOutputStream.write(("POST /api/" + URL + " HTTP/1.1\r\n").getBytes());
            dataOutputStream.write(("Content-Type: " + "application/text\r\n").getBytes());
            dataOutputStream.write(("Content-Length: " + encodedFile.length()+"\r\n").getBytes());
            dataOutputStream.write(("\r\n").getBytes());
            dataOutputStream.write(byteSend);
            dataOutputStream.flush();
            dataOutputStream.write(("\r\n").getBytes());            
            
            // receive Response
            String line = "";
            String imgData = "";
            while((line = bufferedReader.readLine())!= null) 
            {
                imgData += line;
            }
            
            // decode the string base
            String base64 = imgData.substring(imgData.indexOf('\'') + 1, imgData.lastIndexOf('}') - 1);
            byte[] strDecode = Base64.getDecoder().decode(base64);
            
            // display the encoded image
            Image processedImage = new Image(new ByteArrayInputStream(strDecode));
            postImageView.setImage(processedImage);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();        
        }

        finally
        {
            try
            {
                if(fileInputStream != null) fileInputStream.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // public void Effect(String strURL, ImageView postImageView  /*Passing a url string for each button*/) 
	// {
	// 	String enFile = null;
	// 	File imgfile = new File("data/images", "Kiyo.png");
		
		
	// 	//Put the File Content onto the byte array
	// 	byte[] bytes = new byte[(int)imgfile.length()];
		
	// 	try {
			
	// 		FileInputStream fis = new FileInputStream(imgfile);
	// 		try 
	// 		{
	// 			fis.read(bytes);
	// 			enFile = new String(Base64.getEncoder().encodeToString(bytes));
				
	// 			//Send Bytes Across
	// 			byte[] byteSend = enFile.getBytes();
				
	// 			//Do the HTTP POST Command
	// 			dataOutputStream.write(("POST " + strURL + " HTTP/1.1\r\n").getBytes());
	// 			dataOutputStream.write(("Content-Type: "+"application/text\r\n").getBytes());
	// 			dataOutputStream.write(("Content-Length: " + enFile.length()+"\r\n").getBytes());
	// 			dataOutputStream.write(("\r\n").getBytes());
	// 			dataOutputStream.write(byteSend);
	// 			dataOutputStream.flush();
	// 			dataOutputStream.write(("\r\n").getBytes());
	// 			String response = "";
				
	// 			// area.appendText("POST Command sent");
	// 			String line = "";
				
	// 			while(!(line = bufferedReader.readLine()).equals("")) 
	// 			{
	// 				response += line + "\n";
	// 			}
	// 			System.out.println(response);
				
				
	// 			//Receive Response
	// 			String imgData = "";
	// 			while((line = bufferedReader.readLine())!= null) 
	// 			{
	// 				imgData += line;
	// 			}
				
	// 			//Decoded the string base
	// 			String base64 = imgData.substring(imgData.indexOf('\'') + 1, imgData.lastIndexOf('}') - 1);
	// 			System.out.println(base64);
				
	// 			byte[] strDecode = Base64.getDecoder().decode(base64);
				
	// 			//Display the encoded image
				 
	// 			Image grayImg = new Image(new ByteArrayInputStream(strDecode));
	// 			postImageView.setImage(grayImg);
						
	// 		} catch (IOException e1) 
	// 		{
	// 			e1.printStackTrace();
	// 		}
	// 		finally 
	// 		{
	// 			try 
	// 			{
	// 				dataOutputStream.close();
	// 				connectionSocket.close();
	// 			} 
	// 			catch (IOException e1) 
	// 			{
	// 				e1.printStackTrace();
	// 			}
	// 		}
	// 	} 
	// 	catch (FileNotFoundException e1) 
	// 	{
	// 		e1.printStackTrace();
	// 	}
	// }
}
