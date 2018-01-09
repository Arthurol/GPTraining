package gptraining.inicializacao;

import gptraining.model.ArvoreExpressao;
import gptraining.model.No;

import java.util.Random;

/**
 * Geração de uma árvore aleatória através do método Grow. Neste, as folhas não precisam estar na profundidade máxima. A profundidade de cada folha é decidida com sorteios aleatórios. 
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
			//filho à esquerda será operador e filho à direita será terminal
			case 0:
				raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoGrow(profundidadeLimite - 1));
				raizSubArvore.setNoFilhoDireita(geraArvoreMetodoGrow(0));
				break;
				
			//filho à esquerda será terminal e filho à direita será operador
			case 1:
				raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoGrow(0));
				raizSubArvore.setNoFilhoDireita(geraArvoreMetodoGrow(profundidadeLimite - 1));
				break;
				
			//Ambos os filhos serão operadores
			case 2:
				raizSubArvore.setNoFilhoEsquerda(geraArvoreMetodoGrow(profundidadeLimite - 1));
				raizSubArvore.setNoFilhoDireita(geraArvoreMetodoGrow(profundidadeLimite - 1));
				break;
				
			//Ambos os filhos serão terminais
			case 3:
				return geraArvoreMetodoGrow(1);
		
			}
		}
			return raizSubArvore;
			
	}
	
}
