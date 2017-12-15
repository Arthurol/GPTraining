package operacoes;

import java.util.List;
import java.util.Random;

import gptraining.model.ArvoreExpressao;
import gptraining.model.No;

/*
 * Classe responsável pela modificação das árvores de expressão através dos operadores genéticos.
 */
public class OperacaoGenetica {
	
	private int contadorOperacoesCrossover;
	
	public OperacaoGenetica()
	{
		contadorOperacoesCrossover = 0;
	}
	
	public ArvoreExpressao crossover(ArvoreExpressao arvore)
	{
		
		return null;
	}
	
	/*
	 * Crossover onde pedaços de duas árvores de expressão são usados para criar um descendente. Um ponto de crossover é escolhido nas duas árvores
	 * 
	 */
	public ArvoreExpressao subtreeCrossover(ArvoreExpressao arvoreA, ArvoreExpressao arvoreB)
	{
		Random random = new Random();
		
		//Seleciona um ponto aleatório de crossover nas duas árvores
		int pontoCrossoverA = random.nextInt(arvoreA.getQuantidadeOperadores(arvoreA.getRaiz()));
		int pontoCrossoverB = random.nextInt(arvoreB.getQuantidadeOperadores(arvoreB.getRaiz()));
		No subarvoreA = quebraNoPontoCrossover(arvoreA.getRaiz(), pontoCrossoverA);
		No subarvoreB = quebraNoPontoCrossover(arvoreB.getRaiz(), pontoCrossoverB);

		//Sorteio aleatório para decidir qual das raízes das duas subarvores será a raiz da árvore originada
		int sorteio = random.nextInt(2);
		ArvoreExpressao arvoreCombinada = new ArvoreExpressao();
		
		switch (sorteio)
		{
		case 0:
			arvoreCombinada = combina(subarvoreA, subarvoreB);
			break;
			
			
		case 1:
			arvoreCombinada = combina(subarvoreB, subarvoreA);
			break;
		}
		
		//Se a árvore gerada no crossover não possuir um terminal x, o crossover é feito novamente entre as duas árvores iniciais.
		if (!arvoreCombinada.checaExistenciaDeNoX(arvoreCombinada.getRaiz()))
			arvoreCombinada = subtreeCrossover(arvoreA, arvoreB);
		
		return arvoreCombinada;
		
		//Escolher randomicamente qual das raízes das subárvores originadas da quebra será a raiz da árvore originada do crossover?
		
		//Escolher pontos de crossover apenas se a raiz da subárvore gerada for um operador? 
			//Se não, será que é interessante que pontos de crossover em folhas tenham uma probabilidade menor de serem escolhidos?
		
		//Separar o método combina é uma boa ideia?
	}
	
	public ArvoreExpressao sizeFairCrossover(ArvoreExpressao arvoreA, ArvoreExpressao arvoreB)
	{
		//TODO Crossover onde o tamanho da subárvore gerada pela quebra no ponto de crossover (selecionado aleatóriamente, 
			//como no subtree crossover) na primeira árvore é usado para escolher o ponto de crossover na segunda
			//Evita a substituição de uma subárvore pequena por uma muito maior.
			//Vale a pena implementar?
		return null;
	}
	
	public ArvoreExpressao mutacao(ArvoreExpressao arvore)
	{
		// Mutação de ponto ou Mutação de subarvore??
		// Mutação de ponto apenas substituiria um nó qualquer por um nó aleatório de mesmo tipo (operação ou terminal).
		// Mutação de subarvore seria escolher um ponto aleatório de mutação, gerar uma árvore de expressao aleatória e fazer a substituição.
		
		return null;
	}
	
	/*
	 * Expectativa: Método que chamará o cálculo de fitness sobre uma geração (List<ArvoreExpressao>) e a ordenará pela fitness.
	 * 	Após ocorrerá o sorteio para determinar se algum operador genético (crossover ou mutação) será aplicado sobre os indivíduos de melhor fitness.
	 * 	É importante lembrar que o crossover deve ter uma probabilidade de ocorrer bem maior do que a mutação.
	 */
	public List<ArvoreExpressao> selecao()
	{
		return null;
	}
	

	/*
	 * Busca recursiva pelo nó que representa o ponto de crossover sorteado.
	 */
	
/*
	public No quebraNoPontoCrossover(No no, int operadorAtual, int pontoCrossover)
	{
		if (no.getOperador() == null)
			return null;
		
	    if ((operadorAtual == pontoCrossover) && no.getOperador() != null)
	    	return no;
	    
	    if ((operadorAtual != pontoCrossover) && no.getOperador() != null )
	    	operadorAtual++;
	    
	    No noQuebra = quebraNoPontoCrossover(no.getNoFilhoEsquerda(), operadorAtual, pontoCrossover);
	    if (noQuebra == null)
	    	noQuebra = quebraNoPontoCrossover(no.getNoFilhoDireita(), operadorAtual, pontoCrossover);

	    return noQuebra;
	    
	    }
*/
		/*
	    return (quebraNoPontoCrossover(no.getNoFilhoEsquerda(), noAtual, pontoCrossover) == null ? 
	    		quebraNoPontoCrossover(no.getNoFilhoDireita(), noAtual, pontoCrossover) : 
    				quebraNoPontoCrossover(no.getNoFilhoEsquerda(), noAtual, pontoCrossover));   
		*/
	
	
	public No quebraNoPontoCrossover(No no, int pontoCrossover)
	{
		if (no.getOperador() == null)
			return null;
		
	    if ((this.contadorOperacoesCrossover == pontoCrossover) && no.getOperador() != null)
	    	return no;
	    
	    if ((this.contadorOperacoesCrossover != pontoCrossover) && no.getOperador() != null )
	    	this.contadorOperacoesCrossover++;
	    
	    No noQuebra = quebraNoPontoCrossover(no.getNoFilhoEsquerda(), pontoCrossover);
	    if (noQuebra == null)
	    	noQuebra = quebraNoPontoCrossover(no.getNoFilhoDireita(), pontoCrossover);
	    
	    return noQuebra;
    }
	    	
	public boolean compararEstruturaNos(No raizA, No raizB)
	{
		if ((!raizA.getSimboloTerminal().equals(raizB.getSimboloTerminal())) || raizA.getOperador() != raizB.getOperador()) 
			return false;
		
		if ((raizA.possuiFilhos() && !raizB.possuiFilhos()) || (!raizA.possuiFilhos() && raizB.possuiFilhos()))
			return false;
		
		if (raizA.possuiFilhos())
		{
			if (compararEstruturaNos(raizA.getNoFilhoEsquerda(), raizB.getNoFilhoEsquerda()) == false || 
					compararEstruturaNos(raizA.getNoFilhoDireita(), raizB.getNoFilhoDireita()) == false)
				return false;
		}
		return true;		
	}
	
	/*
	 * Substitui aleatoriamente um dos nós filhos da subarvore A pela subarvore B e retorna a árvore resultante.
	 */
	private ArvoreExpressao combina(No subarvoreA, No subarvoreB)
	{
		Random random = new Random();
		int sorteio = random.nextInt(2);
		
		switch(sorteio)
		{
		case 0:
			subarvoreA.setNoFilhoEsquerda(subarvoreB);
		case 1:
			subarvoreA.setNoFilhoDireita(subarvoreB);
		}
		return new ArvoreExpressao(subarvoreA);
	}

}
