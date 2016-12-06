package gptraining.inicializacao;

import gptraining.model.ArvoreExpressao;
import gptraining.model.No;

import java.util.Random;

/**
 * Gera��o de uma �rvore aleat�ria atrav�s do m�todo Grow. Neste a profundidade da �rvore n�o precisa necess�riamente corresponder ao seu limite m�ximo, 
 * al�m da possibilidade de haver folhas em n�veis superiores � profundidade da �rvore.
 */
public class GeradorArvoreMetodoGrow implements IGeradorArvore 
{

	public ArvoreExpressao gerarArvore(int profundidadeLimite)
	{
		if (profundidadeLimite > 0)
		{
			No raiz = new No();			
			raiz = geraArvoreMetodoGrow(profundidadeLimite);
			
			ArvoreExpressao arvore = new ArvoreExpressao(raiz);
			return arvore;
		}
		return null;
	}
	
	private No geraArvoreMetodoGrow(int profundidadeLimite)
	{
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
			raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoGrow(0));
			raizSubArvore.setNoFilhoDireita(geraArvoreMetodoGrow(0));
			
		} else 
		{
			Random random = new Random();
			int decisao = random.nextInt(4);
			
			switch (decisao)
			{
			//filho � esquerda ser� operador e filho a direita ser� terminal
			case 0:
				raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoGrow(profundidadeLimite - 1));
				raizSubArvore.setNoFilhoDireita(geraArvoreMetodoGrow(0));
				break;
				
			//filho � esquerda ser� terminal e filho a direita ser� operador
			case 1:
				raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoGrow(0));
				raizSubArvore.setNoFilhoDireita(geraArvoreMetodoGrow(profundidadeLimite - 1));
				break;
				
			//Ambos os filhos ser�o operadores
			case 2:
				raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoGrow(profundidadeLimite - 1));
				raizSubArvore.setNoFilhoDireita(geraArvoreMetodoGrow(profundidadeLimite - 1));
				break;
				
			//Ambos os filhos ser�o terminais
			case 3:
				return geraArvoreMetodoGrow(1);
		
			}
		}
			return raizSubArvore;
			
	}
	
}
