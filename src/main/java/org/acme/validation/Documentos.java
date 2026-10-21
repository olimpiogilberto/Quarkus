package org.acme.validation;

public final class Documentos {
    private Documentos() {
    }

    public static String digitos(String valor) {
        if (valor == null || !valor.matches("[0-9./-]+")) {
            return "";
        }
        return valor.replaceAll("\\D", "");
    }

    public static boolean cpfValido(String entrada) {
        String cpf = digitos(entrada);
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        int primeiro = digitoCpf(cpf, 9);
        int segundo = digitoCpf(cpf, 10);
        return cpf.charAt(9) - '0' == primeiro && cpf.charAt(10) - '0' == segundo;
    }

    private static int digitoCpf(String cpf, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    public static boolean cnpjValido(String entrada) {
        String cnpj = digitos(entrada);
        if (cnpj.length() != 14 || cnpj.chars().distinct().count() == 1) {
            return false;
        }
        int primeiro = digitoCnpj(cnpj, 12);
        int segundo = digitoCnpj(cnpj, 13);
        return cnpj.charAt(12) - '0' == primeiro && cnpj.charAt(13) - '0' == segundo;
    }

    private static int digitoCnpj(String cnpj, int tamanho) {
        int[] pesos = tamanho == 12
                ? new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2}
                : new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
