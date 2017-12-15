package gptraining.test;

import static org.junit.Assert.*;

import org.junit.Test;

import gptraining.model.ArvoreExpressao;
import gptraining.model.Dataset;
import gptraining.model.No;
import operacoes.OperacaoGenetica;

public class TestOperacoesGeneticas {

	/*
	 * �rvore que representa a express�o x� + 2x� + 5
	 */
	private ArvoreExpressao criarArvoreAlturaQuatro()
	{
		No raiz = new No('+', new No('*', new No("x"), new No('*', new No("x"), new No("x"))), 
				new No('+', new No(5.0), new No('*', new No('*', new No("x"), new No("x")), new No(2.0))));
		return new ArvoreExpressao(raiz);
	}
	
	@Test
	public void testQuebraPontoCrossover() {
		ArvoreExpressao arvoreA = criarArvoreAlturaQuatro();
		
		OperacaoGenetica operacaoGenetica = new OperacaoGenetica();
		No raizA = operacaoGenetica.quebraNoPontoCrossover(arvoreA.getRaiz(), 3);
		No raizB = new No('+', new No(5.0), new No('*', new No('*', new No("x"), new No("x")), new No(2.0))); //�rvore 2x� + 5
		No raizC = new No('*', new No('*', new No("x"), new No("x")), new No(2.0)); //�rvore 2x�
		
		assertTrue(operacaoGenetica.compararEstruturaNos(raizA, raizB));
		assertFalse(operacaoGenetica.compararEstruturaNos(raizB, raizC));
	}
	
	@Test
	public void testContagemOperacoes() {		
		ArvoreExpressao arvore = new ArvoreExpressao();
		
		No raizA = new No('+', new No('/', new No(5.0), new No("x")), new No('*', new No('*', new No("x"), new No("x")), new No(2.0))); //�rvore 5/x + 2x�
		No raizB = new No('*', new No('*', new No("x"), new No("x")), new No(2.0)); //�rvore 2x�
		
		assertEquals(4 ,arvore.getQuantidadeOperadores(raizA));
		assertEquals(2 ,arvore.getQuantidadeOperadores(raizB));
	}
	
	
	// Testar busca recursiva pelo n� de opera��o sorteado. OK
	// Testar contagem de n�s de opera��o numa �rvore. OK
	//TODO Buscar maneira que substitua a utiliza��o de atributos de classe para n�o "perder" o valor do contador durante a recurs�o.
	
	//Como testar o funcionamento do m�todo subtreeCrossover com toda a aleat�riedade existente em seu funcionamento?
	
}
