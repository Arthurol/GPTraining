package operacoes;


import java.util.Collections;
import java.util.Random;
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
		Dataset dataset = new Dataset();
		monta(dataset);
		
		int tamanhoPopulacao = 70;
		int profundidadeLimiteArvores = 4;
		Random random = new Random();
		//random.setSeed(1234567);
		
		Populacao primeiraGeracao = geradorPop.inicializacaoRampedHalfAndHalf(tamanhoPopulacao, profundidadeLimiteArvores, random);
		
		System.out.println("\n\n<GERAÇÃO 0 -------------------------------------------------->");
		for (ArvoreExpressao arvore : primeiraGeracao.getIndividuos())
		{
			System.out.println(arvore.stringExpressao(arvore.getRaiz()));
		}
		System.out.println("<FIM GERAÇÃO 0 -------------------------------------------------->\n");
		
		Populacao proximaGeracao = new Populacao();
		proximaGeracao.setIndividuos(primeiraGeracao.getIndividuos());
		
		for (int i = 0; i < 50; i++)
		{
			proximaGeracao.setIndividuos(operacaoGenetica.selecao(proximaGeracao, dataset, random).getIndividuos());
			proximaGeracao.setNumeroGeracao(proximaGeracao.getNumeroGeracao() + 1);
		}
		
		System.out.println("\n Árvore de melhor aptidão: " + proximaGeracao.getIndividuos().get(0).stringExpressao(proximaGeracao.getIndividuos().get(0).getRaiz()) + " --> " + proximaGeracao.getIndividuos().get(0).getAptidao());
	}
	
	public static void monta (Dataset dataset)
	{
		for (int x = 1; x <= 5; x++)
		{
			//dataset.adiciona(x, (2 * (x * x * x)) + (4 * (x * x)) + (5 * x) - 1);
			//dataset.adiciona(x, (x * x) + 1);
			dataset.adiciona(x, (x * 5) + 2);
			//dataset.adiciona(x, (x * x) - (2 * x) + 1);
			//dataset.adiciona(x, x - 5);
		}
	}

}
