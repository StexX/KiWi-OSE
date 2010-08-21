/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * 
 * 
 */
package kiwi.model.cluster;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 *
 * @author Grandong
 */
public class SpectralClusteringModel implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param sigularValueDecompositionMatrixU
	 * @throws IOException
	 */
	public Dataset[] clusterSpectralClusteringMatrix(SingularValueDecomposition sigularValueDecompositionMatrix) throws IOException {
		List<Instance> instances = new ArrayList<Instance>();
		Matrix sigularValueDecompositionMatrixU = sigularValueDecompositionMatrix.getU();

		
		double[][] sigularValueDecompositionArrayU = sigularValueDecompositionMatrixU.getArray();
		for (int i = 0; i < sigularValueDecompositionMatrixU.getRowDimension(); i++) {
			double[] tagArray = new double[sigularValueDecompositionMatrixU.getColumnDimension()+1];
			for (int j = 0; j < sigularValueDecompositionMatrixU.getColumnDimension(); j++) {
				tagArray[j]=sigularValueDecompositionArrayU[i][j];
			}
			instances.add(new DenseInstance(tagArray,"tag_pos_"+i));
		}
		
		

		Matrix sigularValueDecompositionMatrixV = sigularValueDecompositionMatrix.getV();
		double[][] sigularValueDecompositionArrayV = sigularValueDecompositionMatrixV.getArray();
		for (int i = 0; i < sigularValueDecompositionMatrixV.getRowDimension(); i++) {
			double[] pageArray = new double[sigularValueDecompositionMatrixV.getColumnDimension()+1];
			for (int j = 0; j < sigularValueDecompositionMatrixV.getColumnDimension(); j++) {
				pageArray[j]=sigularValueDecompositionArrayV[i][j];
			}
			instances.add(new DenseInstance(pageArray,"page_pos_"+i));			
		}		
		

		 // Instance instance = new DenseInstance();
		  
		Dataset data = new DefaultDataset(instances);
		// System.out.println(data);
		/* Create a new instance of the KMeans algorithm, with no options
		 * specified. By default this will generate 4 clusters. */
		Clusterer kMeans = new KMeans(5);
		
		/* Cluster the data, it will be returned as an array of data sets, with
		* each dataset representing a cluster. */
		Dataset[] clusters = kMeans.cluster(data);
		
		return clusters;
		/* Create a measure for the cluster quality */
		/* ClusterEvaluation sse= new SumOfSquaredErrors();
		/* Measure the quality of the clustering */
		/* double score=sse.score(clusters);
		System.out.println(score);*/
	
		//FileHandler.exportDataset(data, new File ("resources/iris.txt"));*/
	}
	
	/**
	 * @param xyMatrix
	 * @return
	 * @throws IOException
	 */
	public SingularValueDecomposition computeLeftSigularValueDecompositionMatrix( Matrix xyMatrix) throws IOException {
		Matrix userTagFrequencyMatrix = xyMatrix;
		//userTagFrequencyMatrix.print(5, 4);
		// transform to double array and determine the row and column size
		double[][] dimensionMatrix = userTagFrequencyMatrix.getArray();
		int M = dimensionMatrix.length;
		int N = dimensionMatrix[0].length;
		// calculate the cosine similarity matrix
		double[][] cosineSimilairity2DArray = new double [M][M];
		double sm1 = 0; double sm2 = 0; double sm3= 0;
		for (int i=0; i<M; i++)
		{
			for(int j=i+1; j<M; j++)
			{
				sm1=0; sm2=0; sm3 = 0;
				for (int k = 0; k<N; k++)
				{
					sm1 += dimensionMatrix[i][k]*dimensionMatrix[j][k];
					sm2 += dimensionMatrix[i][k]*dimensionMatrix[i][k];
					sm3 += dimensionMatrix[j][k]*dimensionMatrix[j][k];
				}
				cosineSimilairity2DArray[i][j]=sm1/Math.sqrt(sm2)/Math.sqrt(sm3);
		    	if (new Double(cosineSimilairity2DArray[i][j]).equals(Double.NaN) ){
		    		cosineSimilairity2DArray[i][j] = 0d;
		    	}
			}
		}
		//symmetrize the similarity matrix
		for (int i=0; i<M; i++)
		{
			cosineSimilairity2DArray[i][i]=1;
			for (int j=i+1; j<M; j++)
			{
				cosineSimilairity2DArray[j][i]=cosineSimilairity2DArray[i][j];
			}
		}
		Matrix cosineSimilarityMatrix = new Matrix(cosineSimilairity2DArray);
		//cosineSimilarityMatrix.print(5, 4);
        // spectrum projection
		int columnDimensionSize = cosineSimilarityMatrix.getColumnDimension();
		// calculate D_{ii}=\sigma_j{cs{ij}}
		double [][] sigmaCosineSimilarity2DArray = new double[columnDimensionSize][columnDimensionSize];
		for (int i =0; i<columnDimensionSize; i++)
		{
			for (int j=0; j<columnDimensionSize; j++)
			{
				sigmaCosineSimilarity2DArray[i][i] =	sigmaCosineSimilarity2DArray[i][i]+cosineSimilairity2DArray[i][j];
			}
		}
		Matrix sigmaCosineSimilarityMatrix = new Matrix(sigmaCosineSimilarity2DArray);
		double squaredSigmaCosineSimilarity2DArray[][] = new double[columnDimensionSize][columnDimensionSize];
		for (int i=0; i<columnDimensionSize; i++)
		{
			double dij = sigmaCosineSimilarityMatrix.get(i, i);
			// System.out.println(dij);
			squaredSigmaCosineSimilarity2DArray[i][i] = 1/Math.sqrt(dij);
	    	if (new Double(squaredSigmaCosineSimilarity2DArray[i][i]).equals(Double.NaN) ){
	    		cosineSimilairity2DArray[i][i] = 0d;
	    	}			
		}
		Matrix squaredSigmaCosineSimilarityMatrix = new Matrix(squaredSigmaCosineSimilarity2DArray);
		//squaredSigmaCosineSimilarityMatrix.print(5, 4);
		//calculate D^(-1/2)(D-CS)D^(-1/2)
		Matrix laplacianMatrix = calculateLaplacianMatrix(cosineSimilarityMatrix,sigmaCosineSimilarityMatrix, squaredSigmaCosineSimilarityMatrix);
		//laplacianMatrix.print(5, 4);
		
		SingularValueDecomposition sigularValueDecompositionMatrix = laplacianMatrix.svd();
		return sigularValueDecompositionMatrix;
	}


	/**
	 * @param cosineSimilarityMatrix
	 * @param sigmaCosineSimilarityMatrix
	 * @param squaredSigmaCosineSimilarityMatrix
	 * @return
	 */
	private static Matrix calculateLaplacianMatrix(Matrix cosineSimilarityMatrix, Matrix sigmaCosineSimilarityMatrix,Matrix squaredSigmaCosineSimilarityMatrix) {
		Matrix minusMatrix = sigmaCosineSimilarityMatrix.minus(cosineSimilarityMatrix);
		Matrix timesMatrix = squaredSigmaCosineSimilarityMatrix.times(minusMatrix);
		Matrix laplacianMatrix = timesMatrix.times(squaredSigmaCosineSimilarityMatrix);
		return laplacianMatrix;
	}
	

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SpectralClusteringModel spectralClustering = new SpectralClusteringModel();
		//read data file x means users y means tag frequency
		//BufferedReader su = new BufferedReader(new InputStreamReader(new FileInputStream("resources/test.dat")));
		//Matrix userTagFrequencyMatrix = Matrix.read(su);
        double[][] array = {{1.,2.,3},{4.,5.,6.},{7.,8.,10.}};
        Matrix userTagFrequencyMatrix = new Matrix(array); 
        SingularValueDecomposition singularValueDecomposition = spectralClustering.computeLeftSigularValueDecompositionMatrix(userTagFrequencyMatrix);
        singularValueDecomposition.getU().print(5, 4);
		
	}	
}


