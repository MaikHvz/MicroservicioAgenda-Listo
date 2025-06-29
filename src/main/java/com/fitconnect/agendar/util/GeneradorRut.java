package com.fitconnect.agendar.util;

import java.util.Random;

public class GeneradorRut {
    private static final Random random = new Random();

    public static String generarRUT() {
        int num = 1_000_000 + random.nextInt(24_000_000);
        String rutSinFormato = String.valueOf(num);
        char dv = calcularDigitoVerificador(rutSinFormato);
        return String.format("%s-%c", rutSinFormato, dv);
    }

    private static char calcularDigitoVerificador(String rutSinFormato) {
        int suma = 0;
        int multiplicador = 2;

        for (int i = rutSinFormato.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(rutSinFormato.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resto = suma % 11;
        return (resto == 0) ? '0' : (resto == 1) ? 'K' : (char) (11 - resto + '0');
    }
}