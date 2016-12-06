package gptraining.inicializacao;

import java.util.Random;

import gptraining.model.ArvoreExpressao;
import gptraining.model.No;


/**
 * Geração de uma árvore aleatória completa, onde a profundidade de todas as folhas é a maior possível, de acordo com o limite de profundidade estabelecido.
 * Os terminais só podem ser observados no último nível da árvore. 
 */
public class GeradorArvoreMetodoFull implements IGeradorArvore 
{

	public ArvoreExpressao gerarArvore(int profundidadeLimite)
	{
		if (profundidadeLimite > 0)
		{
			No raiz = new No();			
			raiz = geraArvoreMetodoFull(profundidadeLimite);
			
			ArvoreExpressao arvore = new ArvoreExpressao(raiz);
			return arvore;
		}
		return null;
	}
	
	public No geraArvoreMetodoFull(int profundidadeLimite)
	{
		if (profundidadeLimite < 0)
		{
			System.out.println("Falha ao tentar gerar uma árvore com profundidade negativa.");
			return null;
		}
		
		if (profundidadeLimite == 0)
		{
			No folha = new No();
			folha.preenchimentoAleatorioTerminal();
			return folha;
		}
			
		No raizSubArvore = new No();
		raizSubArvore.preenchimentoAleatorioOperador();
		
		if (profundidadeLimite == 1)
		{
			raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoFull(0));
			raizSubArvore.setNoFilhoDireita(geraArvoreMetodoFull(0));
		
		} else 
		{
			raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoFull(profundidadeLimite - 1));
			raizSubArvore.setNoFilhoDireita(geraArvoreMetodoFull(profundidadeLimite - 1));
		}
		
		return raizSubArvore;
	}
}

