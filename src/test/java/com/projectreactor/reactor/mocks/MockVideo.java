package com.projectreactor.reactor.mocks;

import com.projectreactor.reactor.Video;

import java.util.Arrays;
import java.util.List;

public class MockVideo {

    public static List<Video> generateVideos() {
        return Arrays.asList(
                new Video("Como Programar Java - 1", "Estudos em java", 154, 100),
                new Video("Kotlin para iniciantes - 2", "Começando com kotlin uma abordagem diferente", 200, 300),
                new Video("Estrutura de Dados - 3", "Tipos de estruturas ", 122, 400),
                new Video("Arquitetura de Computadores - 4", "AOC", 499, 600),
                new Video("Redes Neurais - 5", "Estudos de redes neurais ", 388, 900),
                new Video("Machine Learning - 6", "Aprendizado de maquina com python ", 283, 300),
                new Video("Algoritmos e Logica de Programacao - 7", "Estudos sobre logica de programação.", 1, 1)
        );
    }


    public static List<Video> generateVideos2() {
        return Arrays.asList(
                new Video("Nome video 1 - 1", "descricao1", 1, 1),
                new Video("Nome video 2 - 2", "descricao2", 1, 2)
        );
    }


    public static List<Video> generateVideos3() {
        return Arrays.asList(
                new Video("Nome video 1 - 1", "descricao1", 1, null),
                new Video("Nome video 2 - 2", "descricao2", 1, null),
                new Video("Nome video 3 - 3", "descricao3", 1, null)
        );
    }
}
