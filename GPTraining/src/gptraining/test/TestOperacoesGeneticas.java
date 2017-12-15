package gptraining.test;

import static org.junit.Assert.*;

import org.junit.Test;

import gptraining.model.ArvoreExpressao;
import gptraining.model.Dataset;
import gptraining.model.No;
import operacoes.OperacaoGenetica;

public class TestOperacoesGeneticas {

	/*
	 * Árvore que representa a expressão x³ + 2x² + 5
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
		No raizB = new No('+', new No(5.0), new No('*', new No('*', new No("x"), new No("x")), new No(2.0))); //árvore 2x² + 5
		No raizC = new No('*', new No('*', new No("x"), new No("x")), new No(2.0)); //árvore 2x²
		
		assertTrue(operacaoGenetica.compararEstruturaNos(raizA, raizB));
		assertFalse(operacaoGenetica.compararEstruturaNos(raizB, raizC));
	}
	
	@Test
	public void testContagemOperacoes() {		
		ArvoreExpressao arvore = new ArvoreExpressao();
		
		No raizA = new No('+', new No('/', new No(5.0), new No("x")), new No('*', new No('*', new No("x"), new No("x")), new No(2.0))); //árvore 5/x + 2x²
		No raizB = new No('*', new No('*', new No("x"), new No("x")), new No(2.0)); //árvore 2x²
		
		assertEquals(4 ,arvore.getQuantidadeOperadores(raizA));
		assertEquals(2 ,arvore.getQuantidadeOperadores(raizB));
	}
	
	
	// Testar busca recursiva pelo nó de operação sorteado. OK
	// Testar contagem de nós de operação numa árvore. OK
	//TODO Buscar maneira que substitua a utilização de atributos de classe para não "perder" o valor do contador durante a recursão.
	
	//Como testar o funcionamento do método subtreeCrossover com toda a aleatóriedade existente em seu funcionamento?
	
}
