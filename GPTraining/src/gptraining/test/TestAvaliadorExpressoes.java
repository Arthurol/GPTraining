package gptraining.test;

import static org.junit.Assert.assertEquals;
import gptraining.model.ArvoreExpressao;
import gptraining.model.CalculadorFitness;
import gptraining.model.Dataset;
import gptraining.model.No;

import org.junit.Test;

/**
 * Classe de teste para o avaliador de expressões
 * 
 * @author marciobarros
 */
public class TestAvaliadorExpressoes
{
	/**
	 * Dataset que representa a equação Y = x + 1
	 */
	private Dataset criaDatasetLinear()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 2);
		ds.adiciona(2, 3);
		ds.adiciona(3, 4);
		ds.adiciona(4, 5);
		return ds;
	}
	
	/**
	 * Dataset que representa a equação Y = 2x
	 */
	private Dataset criaDatasetMultiplicativo()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 2);
		ds.adiciona(2, 4);
		ds.adiciona(3, 6);
		ds.adiciona(4, 8);
		return ds;
	}
	
	/**
	 * Dataset que representa a equação Y = 2x + 1
	 */
	private Dataset criaDatasetComposto()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 3);
		ds.adiciona(2, 5);
		ds.adiciona(3, 7);
		ds.adiciona(4, 9);
		return ds;
	}
	
	@Test
	public void testLinear()
	{
		Dataset ds = criaDatasetLinear();
		No raiz = new No('+', new No("x"), new No(1.0));
		ArvoreExpressao arvore = new ArvoreExpressao(raiz);
		CalculadorFitness calculador = new CalculadorFitness();
		assertEquals(0.0, calculador.calcula(arvore, ds), 0.01);
	}
	
	// TODO implementar o calculador de fitness baseado em distancia
	
	// TODO confirmar se o calcula da arvore esta funcionando
	
	// TODO implementar os outros casos de teste (multiplicativo, composto, ...)
}