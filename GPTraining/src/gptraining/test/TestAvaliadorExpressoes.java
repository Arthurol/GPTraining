package gptraining.test; 

import gptraining.model.ArvoreExpressao;
import gptraining.model.CalculadorFitness;
import gptraining.model.Dataset;
import gptraining.model.No;
import gptraining.model.Operacao;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.List;

/**
 * Classe de teste para o avaliador de expressıes
 * 
 * @author marciobarros
 */
public class TestAvaliadorExpressoes
{
	/**
	 * Dataset que representa a equa√ß√£o Y = x + 1
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
	 * Dataset que representa a equa√ß√£o Y = 2x
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
	 * Dataset que representa a equa√ß√£o Y = 1/x + 1/2 
	 */
	private Dataset criaDatasetDivisivo()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 1.5);
		ds.adiciona(2, 1);
		ds.adiciona(3, 0.83);
		ds.adiciona(4, 0.75);
		ds.adiciona(5, 0.7);
		return ds;
	}
	
	/**
	 * Dataset que representa a equa√ß√£o Y = 2x + 3
	 */
	private Dataset criaDatasetComposto()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 5);
		ds.adiciona(2, 7);
		ds.adiciona(3, 9);
		ds.adiciona(4, 11);
		
		ds.adiciona(-1, 1);
		ds.adiciona(-2, -1);
		ds.adiciona(-3, -3);
		ds.adiciona(-4, -5);
		return ds;
	}
	
	/**
	 * Dataset que representa a equaÁ„o Y = 3x≤ + 1
	 */
	private Dataset criaDatasetEquacaoSegundoGrau()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 4);
		ds.adiciona(2, 13);
		ds.adiciona(3, 28);
		ds.adiciona(4, 49);
		ds.adiciona(5, 76);
		return ds;
	}
	
	/**
	 * Dataset que representa a equaÁ„o Y = x≥ + 2x≤ + 5
	 */
	private Dataset criaDatasetEquacaoCubica()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 8);
		ds.adiciona(2, 21);
		ds.adiciona(3, 50);
		ds.adiciona(4, 101);
		ds.adiciona(5, 180);
		return ds;
	}
	
	/**
	 * Dataset que representa a equaÁ„o Y = x≥ - 7x
	 */
	private Dataset criaDatasetEquacaoCubicaXNegativo()
	{
		Dataset ds = new Dataset();
		ds.adiciona(-1, 6);
		ds.adiciona(-2, 6);
		ds.adiciona(-3, -6);
		ds.adiciona(-4, -36);
		ds.adiciona(-5, -90);
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
	
	@Test
	public void testComposto()
	{
		Dataset ds = criaDatasetComposto();
		No raiz = new No('+', new No('*', new No("x"), new No(2.0)), new No(3.0)); //arvore 2x + 3
		ArvoreExpressao arvore = new ArvoreExpressao(raiz);
		CalculadorFitness calculador = new CalculadorFitness();
		assertEquals(0.0, calculador.calcula(arvore, ds), 0.01);
	}
	
	@Test
	public void testEquacaoSegundoGrau()
	{
		Dataset ds = criaDatasetEquacaoSegundoGrau();
		No raiz = new No('+', new No('*', new No("x"), new No('*', new No("x"), new No(3.0))), new No(1.0)); //·rvore 3x≤ + 1
		ArvoreExpressao arvore = new ArvoreExpressao(raiz);
		CalculadorFitness calculador = new CalculadorFitness();
		assertEquals(0.0, calculador.calcula(arvore, ds), 0.01);
	}
	
	@Test
	public void testEquacaoCubica()
	{
		Dataset ds = criaDatasetEquacaoCubica();
		No raiz = new No('+', new No('*', new No("x"), new No('*', new No("x"), new No("x"))), 
					new No('+', new No('*', new No('*', new No("x"), new No("x")), new No(2.0)), new No(5.0))); //·rvore x≥ + 2x≤ + 5
		ArvoreExpressao arvore = new ArvoreExpressao(raiz);
		CalculadorFitness calculador = new CalculadorFitness();
		assertEquals(0.0, calculador.calcula(arvore, ds), 0.01);
	}
	
	@Test
	public void testEquacaoCubicaCoeficienteNegativo()
	{
		Dataset ds = criaDatasetEquacaoCubicaXNegativo();
		No raiz = new No('-', new No('*', new No("x"), new No('*', new No("x"), new No("x"))), 
					new No('*', new No(7.0), new No("x"))); //·rvore x≥ - 7x
		ArvoreExpressao arvore = new ArvoreExpressao(raiz);
		CalculadorFitness calculador = new CalculadorFitness();
		assertEquals(0.0, calculador.calcula(arvore, ds), 0.01);
	}
	
	@Test
	public void testDivisivo()
	{
		Dataset ds = criaDatasetDivisivo();
		No raiz = new No('+', new No('/', new No("1"), new No("x")), new No('/', new No(1.0), new No(2.0))); //·rvore 1/x + 1/2 
		ArvoreExpressao arvore = new ArvoreExpressao(raiz);
		CalculadorFitness calculador = new CalculadorFitness();
		assertEquals(0.0, calculador.calcula(arvore, ds), 0.01);
	}
	
	// confirmar se o calcula da arvore esta funcionando. OK
	
	// TODO implementar os outros casos de teste (multiplicativo, composto, ...)
		//mais testes para o c·lculo?

}