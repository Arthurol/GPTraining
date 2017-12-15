package operacoes;

import java.util.List;
import java.util.Random;

import gptraining.model.ArvoreExpressao;
import gptraining.model.No;

/*
 * Classe respons�vel pela modifica��o das �rvores de express�o atrav�s dos operadores gen�ticos.
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
	 * Crossover onde peda�os de duas �rvores de express�o s�o usados para criar um descendente. Um ponto de crossover � escolhido nas duas �rvores
	 * 
	 */
	public ArvoreExpressao subtreeCrossover(ArvoreExpressao arvoreA, ArvoreExpressao arvoreB)
	{
		Random random = new Random();
		
		//Seleciona um ponto aleat�rio de crossover nas duas �rvores
		int pontoCrossoverA = random.nextInt(arvoreA.getQuantidadeOperadores(arvoreA.getRaiz()));
		int pontoCrossoverB = random.nextInt(arvoreB.getQuantidadeOperadores(arvoreB.getRaiz()));
		No subarvoreA = quebraNoPontoCrossover(arvoreA.getRaiz(), pontoCrossoverA);
		No subarvoreB = quebraNoPontoCrossover(arvoreB.getRaiz(), pontoCrossoverB);

		//Sorteio aleat�rio para decidir qual das ra�zes das duas subarvores ser� a raiz da �rvore originada
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
		
		//Se a �rvore gerada no crossover n�o possuir um terminal x, o crossover � feito novamente entre as duas �rvores iniciais.
		if (!arvoreCombinada.checaExistenciaDeNoX(arvoreCombinada.getRaiz()))
			arvoreCombinada = subtreeCrossover(arvoreA, arvoreB);
		
		return arvoreCombinada;
		
		//Escolher randomicamente qual das ra�zes das sub�rvores originadas da quebra ser� a raiz da �rvore originada do crossover?
		
		//Escolher pontos de crossover apenas se a raiz da sub�rvore gerada for um operador? 
			//Se n�o, ser� que � interessante que pontos de crossover em folhas tenham uma probabilidade menor de serem escolhidos?
		
		//Separar o m�todo combina � uma boa ideia?
	}
	
	public ArvoreExpressao sizeFairCrossover(ArvoreExpressao arvoreA, ArvoreExpressao arvoreB)
	{
		//TODO Crossover onde o tamanho da sub�rvore gerada pela quebra no ponto de crossover (selecionado aleat�riamente, 
			//como no subtree crossover) na primeira �rvore � usado para escolher o ponto de crossover na segunda
			//Evita a substitui��o de uma sub�rvore pequena por uma muito maior.
			//Vale a pena implementar?
		return null;
	}
	
	public ArvoreExpressao mutacao(ArvoreExpressao arvore)
	{
		// Muta��o de ponto ou Muta��o de subarvore??
		// Muta��o de ponto apenas substituiria um n� qualquer por um n� aleat�rio de mesmo tipo (opera��o ou terminal).
		// Muta��o de subarvore seria escolher um ponto aleat�rio de muta��o, gerar uma �rvore de expressao aleat�ria e fazer a substitui��o.
		
		return null;
	}
	
	/*
	 * Expectativa: M�todo que chamar� o c�lculo de fitness sobre uma gera��o (List<ArvoreExpressao>) e a ordenar� pela fitness.
	 * 	Ap�s ocorrer� o sorteio para determinar se algum operador gen�tico (crossover ou muta��o) ser� aplicado sobre os indiv�duos de melhor fitness.
	 * 	� importante lembrar que o crossover deve ter uma probabilidade de ocorrer bem maior do que a muta��o.
	 */
	public List<ArvoreExpressao> selecao()
	{
		return null;
	}
	

	/*
	 * Busca recursiva pelo n� que representa o ponto de crossover sorteado.
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
	 * Substitui aleatoriamente um dos n�s filhos da subarvore A pela subarvore B e retorna a �rvore resultante.
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
