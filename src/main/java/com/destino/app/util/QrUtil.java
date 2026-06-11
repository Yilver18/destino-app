package com.destino.app.util;

import com.destino.app.exceptions.DestinoException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class QrUtil {

    private QrUtil() {
    }

    /** Genera un QR del texto dado como bytes PNG (tamaño x tamaño px). */
    public static byte[] generarPng(String texto, int tamano) {
        try {
            BitMatrix matrix = new QRCodeWriter()
                    .encode(texto, BarcodeFormat.QR_CODE, tamano, tamano);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        } catch (WriterException | IOException e) {
            throw new DestinoException("No se pudo generar el código QR", e);
        }
    }
}