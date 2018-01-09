package operacoes;


import java.util.Collections;
import java.util.Scanner;

import gptraining.inicializacao.GeradorPopulacaoInicial;
import gptraining.model.ArvoreExpressao;
import gptraining.model.CalculadorFitness;
import gptraining.model.Dataset;
import gptraining.model.No;
import gptraining.model.Populacao;

public class Main {
	
	public static void main(String[] args)
	{
		GeradorPopulacaoInicial geradorPop =  new GeradorPopulacaoInicial();
		OperacaoGenetica operacaoGenetica = new OperacaoGenetica();
		
		//dataset x + 5
		Dataset dataset = new Dataset();
		dataset.adiciona(1, 6);
		dataset.adiciona(2, 7);
		dataset.adiciona(3, 8);
		dataset.adiciona(4, 9);
		dataset.adiciona(5, 10);

		
		
		int tamanhoPopulacao = 20;
		int profundidadeLimiteArvores = 3;
		
		Populacao primeiraGeracao = geradorPop.inicializacaoRampedHalfAndHalf(tamanhoPopulacao, profundidadeLimiteArvores);
		
		/*
			ArvoreExpressao teste1 = new ArvoreExpressao();
			teste1.setRaiz(populacao.getIndividuos().get(0).getRaiz());
			System.out.println("\nTeste1: " + teste1.stringExpressao(teste1.getRaiz()));
			
			ArvoreExpressao teste2 = new ArvoreExpressao();
			teste2.setRaiz(populacao.getIndividuos().get(1).getRaiz());
			System.out.println("\nTeste2: " + teste2.stringExpressao(teste2.getRaiz()));
			
			ArvoreExpressao resultado = new ArvoreExpressao();
			resultado.setRaiz(operacaoGenetica.combina(teste1, teste2).getRaiz());
			System.out.println("\nResultado: " + resultado.stringExpressao(resultado.getRaiz()));
			
			System.out.println("\nNovamente Teste1: " + teste1.stringExpressao(teste1.getRaiz()));
			System.out.println("\nNovamente Teste2: " + teste2.stringExpressao(teste2.getRaiz()));
		*/
		
		System.out.println("\n\n<GERAÇÃO 0 -------------------------------------------------->");
		for (ArvoreExpressao arvore : primeiraGeracao.getIndividuos())
		{
			System.out.println(arvore.stringExpressao(arvore.getRaiz()));
		}
		System.out.println("<FIM GERAÇÃO 0 -------------------------------------------------->");
		
		Populacao proximaGeracao = new Populacao();
		proximaGeracao.setIndividuos(primeiraGeracao.getIndividuos());
		
		for (int i = 0; i < 9; i++)
		{
			proximaGeracao = operacaoGenetica.selecao(proximaGeracao, dataset);
		}
		
	}

}
