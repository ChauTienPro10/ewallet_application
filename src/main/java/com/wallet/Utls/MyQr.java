package com.wallet.Utls;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MyQr {

	public static String createQR(String data, String charset, Map hashMap, int height, int width)
			throws WriterException, IOException {

		BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

		byte[] imageBytes = outputStream.toByteArray();
		String base64Image = Base64.getEncoder().encodeToString(imageBytes);
		return base64Image;
	}

	public static void main(String[] args) throws WriterException, IOException, NotFoundException {

		Bill bill=new Bill( new BigDecimal(10000), 9);
		Gson gson = new Gson();
        String jsonData = gson.toJson(bill);


		String charset = "UTF-8";

		Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		System.out.println(createQR(jsonData, charset, hashMap, 200, 200));
		String baseString=createQR(jsonData, charset, hashMap, 200, 200);
		
		System.out.println(readQR(convertBase64ToImage(baseString),"UTF-8"));
		saveBase64ImageToFile(baseString);
	}

	

	public static BufferedImage convertBase64ToImage(String base64Image) throws IOException {
		byte[] imageBytes = Base64.getDecoder().decode(base64Image);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
		return ImageIO.read(bis);
	}

	public static String readQR(BufferedImage image, String charset) throws NotFoundException {
		LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultiFormatReader reader = new MultiFormatReader();
        Result result = reader.decode(bitmap);

        return result.getText();
	}
	private static void saveBase64ImageToFile(String base64String) {

        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        try {

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));

 
            File outputFile = new File("image.jpg");
            ImageIO.write(image, "jpg", outputFile);

            System.out.println("Image saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save image");
        }
    }
			

}
class Bill{
	BigDecimal amount;
	int receiver;
	public Bill(BigDecimal amount, int rec) {
		this.amount=amount;
		this.receiver=rec;
	}
}