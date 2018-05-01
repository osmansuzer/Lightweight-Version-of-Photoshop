/**
 * Lightweight Version of Photoshop			
 *
 * Created by Osman Süzer
 */

package application;

import vpt.DoubleImage;
import vpt.Image;

public class ApplyFilter {

	
	private Image and(Image img1, Image img2) {
		
		Image output = img1.newInstance(false);
		
		for (int x = 0; x < img1.getXDim(); x++) {
			for (int y = 0; y < img1.getYDim(); y++) {
				
				for(int c=0; c<img1.getCDim(); ++c) {
					
					if(img1.getXYCDouble(x, y, c) > img2.getXYCDouble(x, y,c))
						output.setXYCDouble(x, y, c, img2.getXYCDouble(x, y,c));
					else
						output.setXYCDouble(x, y,c, img1.getXYCDouble(x, y, c));
				}
			}
		}
		
		return output;
	}

	
	
	private Image or(Image img1, Image img2) {
		
		Image output = img1.newInstance(false);
		for (int x = 0; x < img1.getXDim(); x++) {
			for (int y = 0; y < img1.getYDim(); y++) {
				
				for(int c=0; c<img1.getCDim(); ++c) {
					
					if(img1.getXYCDouble(x, y, c) < img2.getXYCDouble(x, y,c))
						output.setXYCDouble(x, y, c, img2.getXYCDouble(x, y,c));
					else
						output.setXYCDouble(x, y,c, img1.getXYCDouble(x, y, c));
				}	
			}
		}
		
		return output;
	}
	
	private boolean equal(Image img1, Image img2) {
		
		for (int x = 0; x < img1.getXDim(); x++) {
			for (int y = 0; y < img1.getYDim(); y++) {

				for(int c=0; c<img1.getCDim(); ++c) {
					if(img1.getXYCDouble(x, y,c) != img2.getXYCDouble(x, y,c))
						return false;
				}
			}
		}
		
		return true;
	}
	
	

	public Image geodesicDilation(Image F, Image G, double[][] filter) {
		
		Image dilation = dilation(F, filter);
		return and(dilation, G);
	
	}
	
	public Image dilationByReconstruction(Image F, Image G, double[][] filter) {
		
		Image temp = F.newInstance(true);
		Image output = geodesicDilation(F, G, filter);
		
		while(!equal(temp, output)) {
			temp = output.newInstance(true);
			output = geodesicDilation(output, G, filter);
		}
		
		return output;
	}
	
	
	
	public Image geodesicErosion(Image F, Image G, double[][] filter) {
		
		Image erosion = erosion(F, filter);
		return or(erosion, G);
	}
	

	public Image erosionByReconstruction(Image F, Image G, double[][] filter) {
		
		Image temp = F.newInstance(true);
		Image output = geodesicErosion(F, G, filter);
		
		while(!equal(temp, output)) {
			temp = output.newInstance(true);
			output = geodesicErosion(output, G, filter);
		}
		
		return output;
	}
	
	
	public Image applyLowPassFilter(Image img, int N) {
		return applyMeanFilter(img, N);
	}
	
	public Image applyHighPassFilter(Image img, int N, int type) {
		
		double[][] filter = createLowPassFilterH2(N);

		if(type == 1) {
			filter = createLowPassFilterH1(N);
		}
		else if(type == 2) {
			
		}
		else if(type == 3) {
			filter = createLowPassFilterH3(N);
		}
		
		return applyFilter(img, filter);
	}
	
	private double[][] createLowPassFilterH1(int N){
		
		//TODO daha fazla oluþturabilirsen 3x3 yernine NxN yaz
		double[][] filter = new double[3][3];
		
		filter[0][0] = -1;
		filter[0][1] = -1;
		filter[0][2] = -1;
		
		
		filter[1][0] = -1;
		filter[1][1] = 8;
		filter[1][2] = -1;
		
		filter[2][0] = -1;
		filter[2][1] = -1;
		filter[2][2] = -1;

		return filter;
	}
	
	
	private double[][] createLowPassFilterH2(int N){
		
		//TODO daha fazla oluþturabilirsen 3x3 yernine NxN yaz
		double[][] filter = new double[3][3];
		
		filter[0][0] = 0.17;
		filter[0][1] = 0.67;
		filter[0][2] = 0.17;
		
		
		filter[1][0] = 0.67;
		filter[1][1] = -3.33;
		filter[1][2] = 0.67;
		
		filter[2][0] = 0.17;
		filter[2][1] = 0.67;
		filter[2][2] = 0.17;

		return filter;
	}
	
	private double[][] createLowPassFilterH3(int N){
		
		//TODO daha fazla oluþturabilirsen 3x3 yernine NxN yaz
		double[][] filter = new double[3][3];
		
		filter[0][0] = 0;
		filter[0][1] = -1;
		filter[0][2] = 0;
		
		
		filter[1][0] = -1;
		filter[1][1] = 5;
		filter[1][2] = -1;
		
		filter[2][0] = 0;
		filter[2][1] = -1;
		filter[2][2] = 0;

		return filter;
	}
	
	public Image morphologicalGradient(Image img, double[][] filter) {
		
		Image erosion = erosion(img, filter);
		Image dilation = dilation(img, filter);
		
		return subtraction(dilation, erosion);
		
	}
	
	public Image opening(Image img, double[][] filter) {
		
		Image erosion = erosion(img, filter);
		Image dilation = dilation(erosion, filter);
		
		return dilation;
	}
	
	
	public Image closing(Image img, double[][] filter) {
		
		Image dilation = dilation(img, filter);
		Image erosion = erosion(dilation, filter);
		
		return erosion;
	}
	
	

	public Image dilation(Image img, double[][] filter) {
		
		Image output = img.newInstance(false);
		
		
		DoubleImage temp = new DoubleImage(img.getXDim()+filter.length-1, img.getYDim()+filter.length-1, img.getCDim());
		
		for(int x=0;x<temp.getXDim(); ++x) {
			for(int y=0;y<temp.getYDim(); ++y) {
				
				for (int c = 0; c < temp.getCDim(); c++) {
					temp.setXYCDouble(x, y,c, 0.0);
				}
				
			}
		}

		for(int x=0;x<img.getXDim(); ++x) {
			for(int y=0;y<img.getYDim(); ++y) {
				
				for (int c = 0; c < temp.getCDim(); c++) {
					temp.setXYCDouble(x+filter.length/2, y+filter.length/2,c, img.getXYCDouble(x, y,c));
				}		
			}
		}
		
		
		for(int x=0;x<temp.getXDim()-filter.length+1; ++x) {
			for(int y=0;y<temp.getYDim()-filter.length+1; ++y) {
			
				for (int c = 0; c < temp.getCDim(); c++) {
				
					double max=0.0;
					for(int i = 0; i<filter.length; ++i) {
						
						for(int j=0;j<filter.length; ++j) {	
							if((filter[i][j] != 0) && temp.getXYCDouble(x+i, y+j, c) > max)
								max = temp.getXYCDouble(x+i, y+j,c);
						}
						
					}
		
					output.setXYCDouble(x, y, c, max);

				}

			}
		}

		return output;	
	}


	public Image erosion(Image img, double[][] filter) {
		
		Image output = img.newInstance(false);
		
		
		DoubleImage temp = new DoubleImage(img.getXDim()+filter.length-1, img.getYDim()+filter.length-1, img.getCDim());
		
		for(int x=0;x<temp.getXDim(); ++x) {
			for(int y=0;y<temp.getYDim(); ++y) {
				
				for (int c = 0; c < temp.getCDim(); c++) {
					temp.setXYCDouble(x, y,c, 1.0);
				}

			}
		}

		for(int x=0;x<img.getXDim(); ++x) {
			for(int y=0;y<img.getYDim(); ++y) {
				
				for (int c = 0; c < temp.getCDim(); c++) {
					temp.setXYCDouble(x+filter.length/2, y+filter.length/2,c, img.getXYCDouble(x, y,c));
				}

			}
		}
		
		
		for(int x=0;x<temp.getXDim()-filter.length+1; ++x) {
			for(int y=0;y<temp.getYDim()-filter.length+1; ++y) {
			
				
				for (int c = 0; c < temp.getCDim(); c++) {
					double min=1.0;
					for(int i = 0; i<filter.length; ++i) {
						for(int j=0;j<filter.length; ++j) {
							
							if(filter[i][j] != 0 && temp.getXYCDouble(x+i, y+j,c) < min)
								min = temp.getXYCDouble(x+i, y+j,c);

						}
					}
				
					output.setXYCDouble(x, y,c, min);
				}
				
				
			}
		}

		return output;	
	}
	

	public static double[][] squareFilter(int N){
		
		double[][] filter = new double[N][N];
		
		for (int i = 0; i < N; i++) {
			
			for (int j = 0; j < N; j++) {
				filter[i][j] = 1;
			}
		}
		
		return filter;
	}
	
	public static double[][] diskFilter(int N){
			
		double radius = N/2;
		
		double[][] filter = new double[N][N];
		
		for (int i = 0; i < N; i++) {
			
			for (int j = 0; j < N; j++) {
				filter[i][j] = 0;
			}
		}
		
		for(int x = 0; x < N; ++x) {
            for(int y = 0; y < N; ++y) {
            	
                double r = (radius*radius-x*x) + (radius*radius-y*y);
            	
                if(r <= (double)radius) {
                	
                	filter[x][y] = 1;
                }
            }
        }
		
		return filter;
	}
	
	public static double[][] crossFilter(int N){
		
		
		double[][] filter = new double[N][N];
		
		for (int i = 0; i < N; i++) {
			
			for (int j = 0; j < N; j++) {
				filter[i][j] = 0;
			}
		}
		
		for(int x = 0; x < N; ++x) {
            for(int y = 0; y < N; ++y) {
            	
                if(x == N/2 || y == N/2) {
                	
                	filter[x][y] = 1;
                }
            }
        }
		
		return filter;
	}

	
	//point Operation 
	
	public Image inversion(Image img) {
		
		Image output = img.newInstance(false);
			
		for (int x = 0; x < img.getXDim(); x++) {
			for (int y = 0; y < img.getYDim(); y++) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					
					
					output.setXYCDouble(x, y, c, 1.0-img.getXYCDouble(x, y,c));
				}
				
			}
		}
		return output;
	}
	
	
	public Image addition(Image img, double constant) {
		
		Image output = img.newInstance(true);
		
		for (int x = 0; x < img.getXDim(); x++) {
			for (int y = 0; y < img.getYDim(); y++) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					double result = img.getXYCDouble(x, y,c)+constant;
					if(result > 1.0)
						result = 1.0;
					
					if(result < 0.0)
						result = 0.0;
					
					output.setXYCDouble(x, y,c, result);
				}
				
				
			}
		}
		return output;
		
	}
	
	public Image addition(Image img, Image img2) {
		
		Image output = img.newInstance(true);
		
		for (int x = 0; x < img.getXDim(); x++) {
			for (int y = 0; y < img.getYDim(); y++) {
				
				
				for (int c = 0; c < img.getCDim(); c++) {
					double result = img.getXYCDouble(x, y,c)+img2.getXYCDouble(x, y,c);
					
					if(result > 1.0)
						result = 1.0;
					
					if(result < 0.0)
						result = 0.0;
					
					output.setXYCDouble(x, y,c, result);	
				}

			}
		}
		return output;
	}
	
	
	
	public Image subtraction(Image img, double constant) {
		
		Image output = img.newInstance(true);
		
		for (int x = 0; x < img.getXDim(); x++) {
			for (int y = 0; y < img.getYDim(); y++) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					double result = img.getXYCDouble(x, y,c)-constant;
					
					if(result < 0.0)
						result = 0.0;
					
					if(result > 1.0)
						result = 1.0;
					
					output.setXYCDouble(x, y,c, result);	
				}
				
				
				
			}
		}
		return output;
		
	}
	
	public Image subtraction(Image img, Image img2) {
		
		Image output = img.newInstance(true);
		
		for (int x = 0; x < img.getXDim(); x++) {
			for (int y = 0; y < img.getYDim(); y++) {
			
				
				for (int c = 0; c < img.getCDim(); c++) {
					double result = img.getXYCDouble(x, y,c)-img2.getXYCDouble(x, y,c);
					
					if(result < 0.0)
						result = 0.0;
					
					if(result > 1.0)
						result = 1.0;
					
					output.setXYCDouble(x, y,c, result);	
				}
				
				
				
			}
		}
		
		return output;
	}
	
	public Image power(Image img, double pow) {
		Image output = img.newInstance(true);
		
		for (int x = 0; x < img.getXDim(); x++) {
			for (int y = 0; y < img.getYDim(); y++) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					output.setXYCDouble(x, y,c, Math.pow(img.getXYCDouble(x, y,c), pow));
				}

			}
		}
		return output;
		
	}
	
	
	public Image applyUnsharpMasking(Image img, int N, int S) {
				
		Image blurredImage = applyGaussianFilter(img, N, S);
		Image detail = subtraction(img, blurredImage);
		Image output = addition(img, detail);
		return output;
	}
	
	
	public Image applyLaplacianFilter(Image img, int N, int type) {
		
		double[][] filter;
		
		if(type >= 0) {
			filter = createLaplacianFilterPositive(N);	
		}
		else {
			filter = createLaplacianFilterNegative(N);
		}
		
		return applyFilter(img, filter);
	}
	
	private double[][] createLaplacianFilterPositive(int N){
		
		double[][] filter = new double[3][3];
		
		filter[0][0] = 0;
		filter[0][1] = 1;
		filter[0][2] = 0;
		
		
		filter[1][0] = 1;
		filter[1][1] = -4;
		filter[1][2] = 1;
		
		filter[2][0] = 0;
		filter[2][1] = 1;
		filter[2][2] = 0;

		return filter;
	}
	
	
	private double[][] createLaplacianFilterNegative(int N){
		
		double[][] filter = new double[3][3];
		
		filter[0][0] = 0;
		filter[0][1] = -1;
		filter[0][2] = 0;
		
		
		filter[1][0] = -1;
		filter[1][1] = 4;
		filter[1][2] = -1;
		
		filter[2][0] = 0;
		filter[2][1] = -1;
		filter[2][2] = 0;

		return filter;
	}
	
	
	public Image applySobelHorizontalFilter(Image img, int N, double I) {
		double[][] filter = createSobelHorizontalFilter(N, I);
		filter = kernelReflection(filter);
		return applyFilter(img, filter);
	}
	
	private double[][] createSobelHorizontalFilter(int N, double I){
		
		double[][] filter = new double[3][3];
		
		filter[0][0] = -1.0*I;
		filter[0][1] = 0;
		filter[0][2] = 1.0*I;
		
		
		filter[1][0] = -2.0*I;
		filter[1][1] = 0;
		filter[1][2] = 2.0*I;
		
		filter[2][0] = -1*I;
		filter[2][1] = 0;
		filter[2][2] = 1.0*I;

		
		
		return filter;
	}
	
	
	public Image applySobelVerticalFilter(Image img, int N, double I) {
		double[][] filter = createSobelVerticalFilter(N, I);
		filter = kernelReflection(filter);
		return applyFilter(img, filter);
	}
	
	private double[][] createSobelVerticalFilter(int N, double I){
		
		double[][] filter = new double[3][3];
		
		filter[0][0] = 1.0*I;
		filter[0][1] = 2.0*I;
		filter[0][2] = 1.0*I;
		
		
		filter[1][0] = 0;
		filter[1][1] = 0;
		filter[1][2] = 0;
		
		filter[2][0] = -1.0*I;
		filter[2][1] = -2.0*I;
		filter[2][2] = -1.0*I;
	
		return filter;
	}
	
	
	public Image applySobelGradientMagnitude(Image img, int N, double I) {
		
		Image sobelX = applySobelHorizontalFilter(img, N, I);
		Image sobelY = applySobelVerticalFilter(img, N, I);
		
		Image output = img.newInstance(true);
		
		for(int x=0; x<img.getXDim(); ++x) {
			for(int y=0; y<img.getYDim(); ++y) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					output.setXYCDouble(x, y,c, Math.sqrt(Math.pow(sobelX.getXYCDouble(x, y,c),2)+Math.pow(sobelY.getXYCDouble(x, y,c),2)));
				}
				
				
			}
			
		}
				
		return output;
	}

	public Image applySobelDirection(Image img, int N, double I) {
		
		Image sobelX = applySobelHorizontalFilter(img, N, I);
		Image sobelY = applySobelVerticalFilter(img,N,I);
		
		Image output = img.newInstance(true);
		
		for(int x=0; x<img.getXDim(); ++x) {
			for(int y=0; y<img.getYDim(); ++y) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					output.setXYCDouble(x, y,c, Math.abs(sobelX.getXYCDouble(x, y,c))+Math.abs(sobelY.getXYCDouble(x, y,c)));
				}
				
				
			}
			
		}
				
		return output;
	}
	
	
	//apply Gaussian Filter/////////////////////////////////////////////////////////
	public Image applyGaussianFilter(Image img, int N, int S) {
		double[][] filter = createGaussianFilter(N, S);
		return applyFilter(img, filter);
	}
	
	public double[][] createGaussianFilter(int N, int S){
		
		double[][] filter = new double[N][N];
		
		for(int i = 0; i<N; ++i) {
			for(int j=0;j<N; ++j) {
				filter[i][j] = gaussian(i-N/2, j-N/2, S);
			}
		}

		return filter;
	}
	private double gaussian(int x, int y, int s) {
		return (1.0/(2*Math.PI*s*s))*Math.exp(-1*((x*x+y*y)/(2.0*s*s)));
	}
	
	
	public Image applyMeanFilter(Image img, int N) {
		double[][] filter = createMeanFilter(N);
		return applyFilter(img, filter);
	}
	
	private double[][] createMeanFilter(int N){
		
		double[][] filter = new double[N][N];

		for(int i = 0; i<N; ++i) {
			for(int j=0;j<N; ++j) {
				filter[i][j] = 1.0/(N*N);
			}
		}

		return filter;
	}
	
	public Image applyMedianFilter(Image img, int N) {
		
		Image output = img.newInstance(true);
		double[] array = new double[N*N]; 
		
		
		DoubleImage temp = new DoubleImage(img.getXDim()+N-1, img.getYDim()+N-1, img.getCDim());
		
		for(int x=0;x<temp.getXDim(); ++x) {
			for(int y=0;y<temp.getYDim(); ++y) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					temp.setXYCDouble(x, y,c, 0.0);
				}

			}
		}

		for(int x=0;x<img.getXDim(); ++x) {
			for(int y=0;y<img.getYDim(); ++y) {
				for (int c = 0; c < img.getCDim(); c++) {
					temp.setXYCDouble(x+N/2, y+N/2,c, img.getXYCDouble(x, y,c));
				}
				
				
			}
		}
		
		
		for(int x=0;x<temp.getXDim()-N+1; ++x) {
			for(int y=0;y<temp.getYDim()-N+1; ++y) {
			
				
				for (int c = 0; c < img.getCDim(); c++) {
					int count =0;
					for(int i = 0; i<N; ++i) {
						for(int j=0;j<N; ++j) {
							
							array[count] = temp.getXYCDouble(x+i, y+j,c);
							++count;
							
						}
					}
					count=0;
					output.setXYCDouble(x, y,c, getMedian(array));	
				}

			}
		}
		
		
		return output;
		
	}
	
	private double getMedian(double[] array) {

		for(int i=0; i<array.length; ++i) {
			for(int j=0; j<array.length; ++j) {
				if(array[i] < array[j]) {
					double temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}

		return array[array.length/2];
	}
	
	private Image applyFilter(Image img, double[][] filter) {
		
		Image output = img.newInstance(false);
		
		
		DoubleImage temp = new DoubleImage(img.getXDim()+filter.length-1, img.getYDim()+filter.length-1, img.getCDim());

		
		for(int x=0;x<temp.getXDim(); ++x) {
			for(int y=0;y<temp.getYDim(); ++y) {
				for (int c = 0; c < img.getCDim(); c++) {
					temp.setXYCDouble(x, y, c, 0.0);
				}
				
			}
		}

		for(int x=0;x<img.getXDim(); ++x) {
			for(int y=0;y<img.getYDim(); ++y) {
				
				for (int c = 0; c < img.getCDim(); c++) {
					temp.setXYCDouble(x+filter.length/2, y+filter.length/2,c, img.getXYCDouble(x, y,c));
				}
	
			}
		}
		
		
		for(int x=0; x<temp.getXDim()-filter.length+1; ++x) {
			for(int y=0; y<temp.getYDim()-filter.length+1; ++y) {
			
				for (int c = 0; c < img.getCDim(); c++) {
					double total =0.0;
					for(int i = 0; i<filter.length; ++i) {
						for(int j=0;j<filter.length; ++j) {
							
							total += temp.getXYCDouble(x+i, y+j,c)*filter[i][j];
					
						}
					}
					output.setXYCDouble(x, y,c, total);	
				}	
				
			}		
		}
		
		return output;
	}
	
	private double[][] kernelReflection(double kernel[][]){
		
		double[][] output = kernel;
		
		for(int i=0; i<(output.length-1)/2; ++i) {
			for(int j=0; j<output.length; ++j) {
				
				double temp = output[i][j];
				output[i][j] = output[output.length-1-i][j];
				output[output.length-1-i][j] = temp;
			}			
		}

		for(int i=0; i<output.length; ++i) {
			for(int j=0; j<(output.length-1)/2; ++j) {
				
				double temp = output[i][j];
				output[i][j] = output[i][output.length-1-j];
				output[i][output.length-1-j] = temp;
			}			
		}
		return output;
	}


	private double[][] createPassIdealFilter(int N, double radius){
		
		double[][] filter = new double[N][N];
		
		for(int i = 0; i<N; ++i) {
			for(int j=0;j<N; ++j) {
				
				double D = Math.sqrt( (i-N/2)*(i-N/2) + (j-N/2)*(j-N/2) );
				
				if(D<=radius)
					filter[i][j] = 1;
				else
					filter[i][j] = 0;	
			}
		}

		return filter;
	}
	
	private double[][] createLowPassGaussianFilter(int N, double radius){
		
		double[][] filter = new double[N][N];
		
		for(int i = 0; i<N; ++i) {
			for(int j=0;j<N; ++j) {
				
				double D = Math.sqrt( (i-N/2)*(i-N/2) + (j-N/2)*(j-N/2) );
				filter[i][j] = Math.exp((-1.0*D*D)/(2*radius*radius));
			}
		}
		return filter;
	}

	private double[][] createLowPassButterwordFilter(int N, double radius, int order){
		
		double[][] filter = new double[N][N];
		
		for(int i = 0; i<N; ++i) {
			for(int j=0;j<N; ++j) {
				
				double D = Math.sqrt( (i-N/2)*(i-N/2) + (j-N/2)*(j-N/2) );

				filter[i][j] = 1/(1+Math.pow((D/radius), 2*order));
			}
		}

		return filter;
	}

	private double[][] createHighPassButterwordFilter(int N, double radius, int order){
		
		double[][] filter = new double[N][N];
		
		for(int i = 0; i<N; ++i) {
			for(int j=0;j<N; ++j) {
				
				double D = Math.sqrt( (i-N/2)*(i-N/2) + (j-N/2)*(j-N/2) );

				filter[i][j] = 1/(1+Math.pow((radius/D), 2*order));
			}
		}

		return filter;
	}
	
	private double[][] createHighPassGaussianFilter(int N, double radius){
		
		double[][] filter = new double[N][N];
		
		for(int i = 0; i<N; ++i) {
			for(int j=0;j<N; ++j) {
				
				double D = Math.sqrt( (i-N/2)*(i-N/2) + (j-N/2)*(j-N/2) );

				filter[i][j] = 1 - Math.exp((-1.0*D*D)/(2*radius*radius));
			}
		}
		return filter;
	}
}
