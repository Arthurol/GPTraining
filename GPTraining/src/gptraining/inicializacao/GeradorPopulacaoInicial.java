package gptraining.inicializacao;

import gptraining.model.ArvoreExpressao;
import gptraining.model.Populacao;

public class GeradorPopulacaoInicial {

	/**
	 * Inicializa a metade da população utilizando o método Grow e a outra metade utilizando o método Full, 
	 * ambos métodos de construção de árvores aleatórias.
	 * 
	 */
	public Populacao inicializacaoRampedHalfAndHalf(int tamanhoPopulacao, int profundidadeLimite)
	{
		Populacao populacaoInicial =  new Populacao(tamanhoPopulacao);
		GeradorArvoreMetodoGrow geradorGrow = new GeradorArvoreMetodoGrow();
		GeradorArvoreMetodoFull geradorFull= new GeradorArvoreMetodoFull();

		int quantidadeIndividuosGrow = tamanhoPopulacao / 2;
		int quantidadeIndividuosFull = tamanhoPopulacao - quantidadeIndividuosGrow;
		
		//Preenchimento da população inicial equilibrando o número de invivíduos gerados aleatoriamente pelos métodos Grow e Full.
		
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
