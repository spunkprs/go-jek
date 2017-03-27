package com.prateek.go_jek_assignment;

import com.prateek.go_jek_processor_api.OutputWriter;

public class ShellBasedOutputWriter implements OutputWriter {
	
	public void writeOutput(String output) {
		System.out.println(output);
	}
}
