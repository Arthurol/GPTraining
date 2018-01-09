package operacoes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import gptraining.model.ArvoreExpressao;
import gptraining.model.CalculadorFitness;
import gptraining.model.Dataset;
import gptraining.model.No;
import gptraining.model.Operacao;
import gptraining.model.Populacao;

/*
 * Classe responsável pela modificação das árvores de expressão através dos operadores genéticos.
 */
public class OperacaoGenetica {
	
	private int contadorOperacoesCrossover;
	private int contadorNosMutacao;
	private static double probabilidadeCrossover = 0.85;
	
	public OperacaoGenetica()
	{
		contadorOperacoesCrossover = 0;
		contadorNosMutacao = 0;
	}
	
	/**
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
		contadorOperacoesCrossover = 0;
		No subarvoreB = quebraNoPontoCrossover(arvoreB.getRaiz(), pontoCrossoverB);

		ArvoreExpressao ArvoreA = new ArvoreExpressao(subarvoreA);
		ArvoreExpressao ArvoreB = new ArvoreExpressao(subarvoreB);
		
		//Sorteio aleatório para decidir qual das raízes das duas subarvores será a raiz da árvore originada
		int sorteio = random.nextInt(2);
		ArvoreExpressao arvoreCombinada = new ArvoreExpressao();
		
		System.out.println("\nÁrvores selecionadas para o crossover: \nArvore A: " + arvoreA.stringExpressao(arvoreA.getRaiz()));
		System.out.println("Arvore B: " + arvoreB.stringExpressao(arvoreB.getRaiz()));
		
		if (sorteio == 0)
			arvoreCombinada = combina(ArvoreA, ArvoreB);
		else
			arvoreCombinada = combina(ArvoreB, ArvoreA);
		
		//Se a árvore gerada no crossover não possuir um terminal x, o crossover é feito novamente entre as duas árvores iniciais.
		if (!arvoreCombinada.checaExistenciaDeNoX(arvoreCombinada.getRaiz()))
		{
			System.out.println("Inexistência de terminal x na árvore descendente");
			contadorOperacoesCrossover = 0;
			return null;
		}
		
		System.out.println("Árvore descendente (A x B): " + arvoreCombinada.stringExpressao(arvoreCombinada.getRaiz()));
		contadorOperacoesCrossover = 0;
		return arvoreCombinada;
	}
	
	/**
	 * Método que realiza uma mutação de ponto(point mutation)
	 */
	public ArvoreExpressao mutacao(ArvoreExpressao arvore)
	{
		int quantidadeNos = arvore.getTamanhoArvore(arvore.getRaiz());
		Random random = new Random();
		int pontoMutacao = random.nextInt(quantidadeNos);
		mutacaoPontoAleatorio(arvore.getRaiz(), pontoMutacao);
		
		contadorNosMutacao = 0;
		return arvore;
	}
	
	/**
	 * Método que percorre a árvore até encontrar o nó representado pelo ponto de mutação. Ao achá-lo, realiza sua substituição 
	 * por um elemento aleatório equivalente (operador por operador e terminal por terminal)
	 */
	public boolean mutacaoPontoAleatorio(No noRaiz, int pontoMutacao) 
	{
		if (noRaiz == null)
			return false;
		
	    if (this.contadorNosMutacao == pontoMutacao)
	    {	
	    	if (noRaiz.getOperador() != null)
	    	{
	    		noRaiz.preenchimentoAleatorioOperador();
	    		this.contadorNosMutacao = 0;
	    		return true;
	    	}
	    	else if (noRaiz.getSimboloTerminal() != null)
	    	{
	    		noRaiz.preenchimentoAleatorioTerminal();
	    		this.contadorNosMutacao = 0;
	    		return true;
	    	}
	    	else
	    		return false;    		
	    }
	   
	    if (this.contadorNosMutacao != pontoMutacao) 
	    	this.contadorNosMutacao++;
	    
	    if (!mutacaoPontoAleatorio(noRaiz.getNoFilhoEsquerda() ,pontoMutacao))
	    {
	    	return mutacaoPontoAleatorio(noRaiz.getNoFilhoDireita(), pontoMutacao);
	    }

		return true;
	}

	/**
	 * Expectativa: Método que chamará o cálculo de fitness sobre uma geração (List<ArvoreExpressao>) e a ordenará pela fitness.
	 * Após ocorrerá o sorteio para determinar se algum operador genético (crossover ou mutação) será aplicado sobre os indivíduos de melhor fitness.
	 * É importante lembrar que o crossover deve ter uma probabilidade de ocorrer bem maior do que a mutação.
	 */
	public Populacao selecao(Populacao populacao, Dataset dataset)
	{
		CalculadorFitness calculador = new CalculadorFitness();
		
		//faz o calculo do fitness de toda a população
		for (ArvoreExpressao arvore : populacao.getIndividuos())
		{
			arvore.setAptidao(calculador.calcula(arvore, dataset) == -1 ? 1000000 : calculador.calcula(arvore, dataset));
		}
		
		//Ordena a população pela aptidão (fitness). Quanto mais próxima de zero for a aptidão, melhor é a árvore (Distância entre dois pontos)
		Collections.sort(populacao.individuos);
		
		Populacao proximaGeracao = new Populacao();
		proximaGeracao.setNumeroGeracao(populacao.getNumeroGeracao() + 1);
		int tamanhoPopulacao = populacao.getIndividuos().size();
		
		for (int i = 0; i < (tamanhoPopulacao * 0.3); i++)
			proximaGeracao.adicionaIndividuo(populacao.getIndividuos().get(i));
			
		
		while(proximaGeracao.getIndividuos().size() < populacao.getIndividuos().size())
		{
			Random random = new Random();
			double escolhaOperadorGenetico = random.nextDouble();
			ArvoreExpressao arvoreProduto = new ArvoreExpressao();
			
			if (escolhaOperadorGenetico < probabilidadeCrossover)
			{
				while(true)
				{
					List<ArvoreExpressao> candidatosA = selecaoPorTorneio(populacao);
					List<ArvoreExpressao> candidatosB = selecaoPorTorneio(populacao);
					
					int paiA = random.nextInt(candidatosA.size());
					int paiB = random.nextInt(candidatosB.size());
					
					ArvoreExpressao arvoreA = new ArvoreExpressao(candidatosA.get(paiA).getRaiz());
					ArvoreExpressao arvoreB = new ArvoreExpressao(candidatosB.get(paiB).getRaiz());
					
					if (arvoreA == null || arvoreB == null)
					{
						System.out.println("Um dos selecionados para o crossover é null");
					}
					try
					{
						//quebra-galho temporário, compararEstruturaNos com problema de null pointer
						arvoreA.setAptidao(calculador.calcula(arvoreA, dataset) == -1 ? 1000000 : calculador.calcula(arvoreA, dataset));
						arvoreB.setAptidao(calculador.calcula(arvoreB, dataset) == -1 ? 1000000 : calculador.calcula(arvoreB, dataset));
						
						//if (!arvoreProduto.compararEstruturaNos(arvoreA.getRaiz(), arvoreB.getRaiz()))
						if (arvoreA.getAptidao() != arvoreB.getAptidao())
						{
							arvoreProduto = subtreeCrossover(arvoreA, arvoreB);
							
							if (arvoreProduto != null)
							{
								if (arvoreProduto.checaValidadeArvore(arvoreProduto.getRaiz()) && arvoreProduto.checaExistenciaDeNoX(arvoreProduto.getRaiz()))
								{
									break;
								}
							}
						}
					} catch (Exception e)
					{
						e.printStackTrace();
						System.out.println(arvoreA.stringExpressao(arvoreA.getRaiz()));
						System.out.println(arvoreB.stringExpressao(arvoreB.getRaiz()));
					}
				}
			}
			else
			{
				while(true)
				{
					List<ArvoreExpressao> candidatosMutacao = selecaoPorTorneio(populacao);
					arvoreProduto = mutacao(candidatosMutacao.get(random.nextInt(candidatosMutacao.size())));
					if (arvoreProduto.checaValidadeArvore(arvoreProduto.getRaiz()) && arvoreProduto.checaExistenciaDeNoX(arvoreProduto.getRaiz()))
						break;
				}
			}		
			proximaGeracao.adicionaIndividuo(arvoreProduto);	
		}
		
		for (ArvoreExpressao arv : proximaGeracao.individuos)
		{
			arv.setAptidao(calculador.calcula(arv, dataset) == -1 ? 1000000 : calculador.calcula(arv, dataset));
		}
		
		Collections.sort(proximaGeracao.individuos);
		
		System.out.println("\n\n <GERAÇÃO " + String.valueOf(proximaGeracao.getNumeroGeracao())  + "-------------------------------------------------->");
		for (ArvoreExpressao arvoreExpressao : proximaGeracao.getIndividuos())
		{
			System.out.println(arvoreExpressao.stringExpressao(arvoreExpressao.getRaiz()) + " --> " + arvoreExpressao.getAptidao());
		}
		
		return proximaGeracao;
	}
	
	
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
	
	/**
	 * Substitui aleatoriamente um dos nós filhos da subarvore A pela subarvore B e retorna a árvore resultante.
	 */
	public ArvoreExpressao combina(ArvoreExpressao subarvoreA, ArvoreExpressao subarvoreB)
	{
		ArvoreExpressao arvoreResultado = new ArvoreExpressao(subarvoreA.getRaiz());
		arvoreResultado.getRaiz().setNoFilhoEsquerda(subarvoreB.getRaiz());
			
		return arvoreResultado;
	}
	
	private List<ArvoreExpressao> selecaoPorTorneio(Populacao populacao)
	{
		Random random =  new Random();
		double escolhaGrupo = random.nextDouble();
		List<ArvoreExpressao> grupoTorneio = new ArrayList<ArvoreExpressao>();
		
		
		//Torneio formado pela metade da população. Subdivisão dessa metade em cinco grupos de quantidade igual de indivíduos. 
		//A probabilidade de escolha de cada grupo é a seguinte: g1 - 40%, g2 - 20%, g3 - 20%, g4 - 10, g5 - 10%
		if (escolhaGrupo >= 0 && escolhaGrupo < 0.4)
			grupoTorneio = getGrupoParticipantes(1, populacao.getIndividuos());
		
		else if (escolhaGrupo >= 0.4 && escolhaGrupo < 0.6)
			grupoTorneio = getGrupoParticipantes(2, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.6 && escolhaGrupo < 0.8)
			grupoTorneio = getGrupoParticipantes(3, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.8 && escolhaGrupo < 0.9)
			grupoTorneio = getGrupoParticipantes(4, populacao.getIndividuos());
			
		else if (escolhaGrupo >= 0.9 && escolhaGrupo < 1.0)
			grupoTorneio = getGrupoParticipantes(5, populacao.getIndividuos());
		
		return grupoTorneio;
	}
	
	/**
	 * Divisão abstrata da primeira metade da população passada nos parâmetros em cinco grupos de quantidade igual de indivíduos. 
	 * O método retorna o grupo de indivíduos correspondente ao inteiro passado (chamada com numeroGrupo == 1 retorna os primeiros 10% de indivíduos)
	 */
	private List<ArvoreExpressao> getGrupoParticipantes(int numeroGrupo, List<ArvoreExpressao> individuosPopulacao)
	{
		int tamanhoGrupo = individuosPopulacao.size()/10; //quinta parte da metade da população
		List<ArvoreExpressao> grupoSelecionado = new ArrayList<ArvoreExpressao>();
		
		for(int i = numeroGrupo * tamanhoGrupo; i < (numeroGrupo * tamanhoGrupo) + tamanhoGrupo; i++)
		{
			grupoSelecionado.add(individuosPopulacao.get(i - 1));
		}

		return grupoSelecionado;
	}
}
