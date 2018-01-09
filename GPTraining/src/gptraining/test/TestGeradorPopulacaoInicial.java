package gptraining.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gptraining.inicializacao.GeradorArvoreMetodoFull;
import gptraining.inicializacao.GeradorPopulacaoInicial;
import gptraining.model.ArvoreExpressao;
import gptraining.model.No;
import gptraining.model.Populacao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestGeradorPopulacaoInicial {

	@Test
	public void test1() 
	{
		GeradorPopulacaoInicial geradorPop = new GeradorPopulacaoInicial();
		GeradorArvoreMetodoFull geradorFull = new GeradorArvoreMetodoFull();
		
		List<ArvoreExpressao> populacaoTeste =  new ArrayList<ArvoreExpressao>();
		
		for (int j = 0; j < 50; j++)
		{
			ArvoreExpressao arvoreFull = new ArvoreExpressao();
			while(true)
			{
				arvoreFull = geradorFull.gerarArvore(3);
				
				if (arvoreFull.checaExistenciaDeNoX(arvoreFull.getRaiz()))
					break;
			}
			populacaoTeste.add(arvoreFull);
		}
		No raizTeste = new No();
		raizTeste.noFilhoEsquerda = new No();
		raizTeste.noFilhoDireita = new No();
		
		raizTeste.preenchimentoAleatorioOperador();
		raizTeste.noFilhoEsquerda.preenchimentoAleatorioTerminal();
		raizTeste.noFilhoDireita.preenchimentoAleatorioTerminal();
		
		ArvoreExpressao patinhoFeio = new ArvoreExpressao(raizTeste);
		
		populacaoTeste.add(patinhoFeio);
		
		Populacao pop = new Populacao(populacaoTeste);
		
		assertEquals(50, contaArvoresFull(pop, 15));
		
	}
	
	
	public void test2() 
	{
		GeradorPopulacaoInicial geradorPop = new GeradorPopulacaoInicial();
		Populacao pop = geradorPop.inicializacaoRampedHalfAndHalf(100, 2);
		
		assertTrue(contaArvoresFull(pop, 7) >= 50);
		
		Populacao pop2 = geradorPop.inicializacaoRampedHalfAndHalf(10, 1);
		assertFalse(contaArvoresFull(pop2, 3) == 0);

	}

	public int contaArvoresFull(Populacao pop, int quantidadeNosDasArvores)
	{
		int contador = 0;
		for(int i = 0; i < pop.individuos.size(); i++)
		{
			if(checaNumeroNos(pop.individuos.get(i), quantidadeNosDasArvores))
					contador++;
		}
		return contador;
	}
	
	public boolean checaNumeroNos(ArvoreExpressao arvore, int quantidadeEsperada)
	{
		No raiz = arvore.getRaiz();
		if (Integer.compare(quantidadeEsperada, contadorNos(raiz)) == 0)
			return true;
		
		return false;
				
	}
	
	public int contadorNos(No raiz)
	{
		if (raiz.getNoFilhoEsquerda() == null && raiz.getNoFilhoDireita() == null)
			return 1;
		
		if (raiz.getNoFilhoEsquerda() != null && raiz.getNoFilhoDireita() != null)
		{
			return 1 + contadorNos(raiz.getNoFilhoEsquerda()) + contadorNos(raiz.getNoFilhoDireita());
		}
		return 0;
	}
}
