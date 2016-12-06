package gptraining.inicializacao;

import gptraining.model.ArvoreExpressao;
import gptraining.model.No;


/**
 * Gera��o de uma �rvore aleat�ria completa, onde a profundidade de todas as folhas � a maior poss�vel, de acordo com o limite de profundidade estabelecido.
 * Os terminais s� podem ser observados no �ltimo n�vel da �rvore. 
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
			System.out.println("Falha ao tentar gerar uma �rvore com profundidade negativa.");
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

