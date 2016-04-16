package com.mesutgolcuk.ThreadTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Test {

	static ArrayList<String> questions;
	static ArrayList<String> answers;
	static String[] competitorAnswers;
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		int competitorNumber;

		Scanner scanner = new Scanner(System.in);
		do{
			System.out.println("(1-10)Enter the competitor number:");
			competitorNumber = scanner.nextInt();
		}while( competitorNumber>10 || competitorNumber<1 );
		scanner.close();
		questions = new ArrayList<>();
		answers = new ArrayList<>();
		competitorAnswers = new String[competitorNumber];
		Scanner sc = null;
		for(int j=0;j<competitorAnswers.length;j++){
			competitorAnswers[j] = "0";
		}
		try{
			sc = new Scanner(new FileInputStream("questions.txt"));
			while(sc.hasNext()){
				questions.add(sc.nextLine());
				answers.add(sc.nextLine());
			}
			sc.close();
		}
		catch( IOException e){
			e.printStackTrace();
		}
		shuffleQuestions();
		
		Announcer announcer =  new Announcer(questions,answers,competitorAnswers,competitorNumber);
		Thread th = new Thread(announcer);
		th.start();
		
		
	}
	/**
	 * shuffles the question
	 */
	public static void shuffleQuestions(){
		int questionNumber = questions.size();
		Random rand = new Random();
		for(int i=0;i<questionNumber; i++){
			int index = rand.nextInt(questionNumber);
			questions.add(questions.get(i));
			questions.set(i, questions.get(index));
			questions.remove(index);
			answers.add(answers.get(i));
			answers.set(i, answers.get(index));
			answers.remove(index);
		}
	}


}
