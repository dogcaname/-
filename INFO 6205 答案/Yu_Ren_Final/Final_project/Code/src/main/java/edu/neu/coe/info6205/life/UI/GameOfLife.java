package edu.neu.coe.info6205.life.UI;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

public class GameOfLife extends Canvas implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int frameSize = 450;
	public static String title = "Game of Life";
	
	public Random r = new Random();
	public int gridSize = 100;
	public double generationSpeed = 10.0;
	
	public BufferedImage image;
	public int[] pixels;
	
	public boolean[] pGrid;
	public boolean[] cGrid;
	
	
	public GameOfLife() 
	{
		Dimension d = new Dimension(frameSize, frameSize);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		
		
		image = new BufferedImage(gridSize, gridSize, BufferedImage.TYPE_INT_BGR);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
	}
	
	public void start(String datacsv)
	{
		cGrid = new boolean[pixels.length];
		pGrid = new boolean[pixels.length];
		
//		for(int i = 0; i < cGrid.length; i ++)
//		{
//			cGrid[i] = r.nextInt(100) / 100.0 > 0.80 ? true: false;
//		}
		
		// 3 3, 1 -3, 0 4, 0 1, -2 -2, 0 -4, 0 -2, -2 1, 1 3, 0 0
//		String datacsv = " 4 0, 0 2, 4 -7, 5 -4, -9 -1, 6 -4, -3 7, -8 -3, 0 -8, -9 3, 7 1, 3 0, 4 1, -5 0, 5 4, -7 7, -7 3, -6 8, 4 -2, -4 0";
		int len = 50;
		String[] fields = datacsv.split(",");
		for(int i = 0; i < fields.length; i++) {
			String[] cmp = fields[i].split(" ");
			cGrid[gridSize * (Integer.parseInt(cmp[1]) + len) + (Integer.parseInt(cmp[2]) + len)] = true;
		}
		
//		cGrid[3] = true;
//		cGrid[4] = true;
//		cGrid[5] = true;
		
		new Thread(this).start();
		
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		long currentTime = System.nanoTime();
		long previouseTime = currentTime;
		long passedTime = 0;
		
		double unprocessedTime = 0.0;
		
		long frameCounter = System.currentTimeMillis();
		int generations = 1;
		
		
		while(true)
		{
			double frameCut = 1000000000000.0 / generationSpeed;
			
			previouseTime = currentTime;
			currentTime = System.nanoTime();
			passedTime += currentTime - previouseTime;
			
			unprocessedTime += passedTime;
			
			if(unprocessedTime > frameCut)
			{
				unprocessedTime = 0;
				update();
				generations++;
				
			}
			
			if(System.currentTimeMillis() - frameCounter >= 1000)
			{
				frameCounter = System.currentTimeMillis();
				System.out.println("Generation : " + generations);
			}
			
			render();
			
		}
		
	}
	
	private void render()
	{
		// TODO Auto-generated method stub
		BufferStrategy bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		for(int i = 0; i < pixels.length; i ++)
			pixels[i] = 0;
		
		for(int i = 0; i < pixels.length; i ++)
			pixels[i] = cGrid[i] ? 0xffffff: 0;
		
		g.drawImage(image, 0, 0, frameSize, frameSize, null);
		g.dispose();
		bs.show();
		
		
	}

	private void update()
	{
		// TODO Auto-generated method stub
		for(int i = 0; i < pixels.length; i++)
			pGrid[i] = cGrid[i];
		
		for(int y = 0; y < gridSize; y++)
		{
			for(int x = 0; x < gridSize; x++)
			{
				int res = 0;
				
				int xx0 = x - 1;
				int yy0 = y - 1;
				int xx1 = x + 1;
				int yy1 = y + 1;
				
				if(x != 0)
					res += pGrid[xx0 + gridSize * y] ? 1: 0;
				if(y != 0)
					res += pGrid[x + gridSize * yy0] ? 1: 0;
				if(x != gridSize - 1)
					res += pGrid[xx1 + gridSize * y] ? 1: 0;
				if(y != gridSize - 1)
					res += pGrid[x + gridSize * yy1] ? 1: 0;
				if(x != 0 && y != 0)
					res += pGrid[xx0 + gridSize * yy0] ? 1: 0;
				if(x != 0 && y != gridSize - 1)
					res += pGrid[xx0 + gridSize * yy1] ? 1: 0;
				if(x != gridSize - 1 && y != 0)
					res += pGrid[xx1 + gridSize * yy0] ? 1: 0;
				if(x != gridSize - 1 && y != gridSize - 1)
					res += pGrid[xx1 + gridSize * yy1] ? 1: 0;
				
				
				
				//Game of Life Rules!!!
			
				if(!(pGrid[x + gridSize * y] && (res == 3 || res == 2)))
					cGrid[x + gridSize * y] = false;
				if(!pGrid[x + gridSize * y] && res == 3)
					cGrid[x + gridSize * y] = true;
				
				
			}
		}
		
	}

	public static void main(String[] args)
	{
		
		String[] datas = new String[] {" 4 0, 0 2, 4 -7, 5 -4, -9 -1, 6 -4, -3 7, -8 -3, 0 -8, -9 3, 7 1, 3 0, 4 1, -5 0, 5 4, -7 7, -7 3, -6 8, 4 -2, -4 0",
				" -2 2, -8 -2, 4 3, 4 0, -6 -4, -3 9, -6 6, -9 -8, -1 -5, 4 1, -3 -7, 8 4, -1 6, 3 7, 7 4, -5 6, 5 2, -9 4, 7 2, 8 0",
				" 3 3, 1 -3, 0 4, 0 1, -2 -2, 0 -4, 0 -2, -2 1, 1 3, 0 0"};

		for(String data : datas) {
			JFrame frame = new JFrame();
			frame.setTitle(title);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setAlwaysOnTop(true);
			GameOfLife gol = new GameOfLife();
			frame.add(gol);
			frame.pack();

			frame.setVisible(true);

			gol.start(data);

		}
		
		
	}


}
