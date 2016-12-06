package gptraining.inicializacao;

import gptraining.model.ArvoreExpressao;
import gptraining.model.Populacao;

public class GeradorPopulacaoInicial {

	/**
	 * Inicializa a metade da popula��o utilizando o m�todo Grow e a outra metade utilizando o m�todo Full, 
	 * ambos m�todos de constru��o de �rvores aleat�rias.
	 * 
	 */
	public Populacao inicializacaoRampedHalfAndHalf(int tamanhoPopulacao, int profundidadeLimite)
	{
		Populacao populacaoInicial =  new Populacao(tamanhoPopulacao);
		GeradorArvoreMetodoGrow geradorGrow = new GeradorArvoreMetodoGrow();
		GeradorArvoreMetodoFull geradorFull= new GeradorArvoreMetodoFull();

		int quantidadeIndividuosGrow = tamanhoPopulacao / 2;
		int quantidadeIndividuosFull = tamanhoPopulacao - quantidadeIndividuosGrow;
		
		//Preenchimento da popula��o inicial equilibrando o n�mero de inviv�duos gerados aleatoriamente pelos m�todos Grow e Full.
		
		for (int i = 0; i < quantidadeIndividuosGrow; i++)
		{
			ArvoreExpressao arvoreGrow = new ArvoreExpressao();
			while(true)
			{
				arvoreGrow = geradorGrow.gerarArvore(profundidadeLimite);
				
				if (arvoreGrow.checaExistenciaDeNoX(arvoreGrow.getRaiz()))
					break;
			}
			populacaoInicial.adicionaIndividuo(arvoreGrow);
		}
		
		for (int j = 0; j < quantidadeIndividuosFull; j++)
		{
			ArvoreExpressao arvoreFull = new ArvoreExpressao();
			while(true)
			{
				arvoreFull = geradorFull.gerarArvore(profundidadeLimite);
				
				if (arvoreFull.checaExistenciaDeNoX(arvoreFull.getRaiz()))
					break;
			}
			populacaoInicial.adicionaIndividuo(arvoreFull);
		}
		
		populacaoInicial.embaralhaPopulacao();
		
		return populacaoInicial;
	}
}
