package ai.llmchat.common.langchain.rag;

public interface PromptConstants {

	String QA_CONTEXT_PROMPT = """
			<DOCUMENTS>
			{{contents}}
			</DOCUMENTS>

			<INSTRUCTIONS>
			- All contents between <DOCUMENTS> and </DOCUMENTS> are reference information retrieved from an external knowledge base.
			- Now, answer the following question based on the above retrieved documents(Let's think step by step):
			{{userMessage}}
			When answer to user:
			- If you don't know, just say that you don't know.
			- If you don't know when you are not sure, ask for clarification.
			Avoid mentioning that you obtained the information from the context.
			And answer according to the language of the question.
			</INSTRUCTIONS>
			""";

	String PROMPT_GENERATE_TEMPLATE = """
			<INSTRUCTIONS>
			Please optimize the prompt based on the following task description to make it clearer, more specific, and fully aligned with the task objectives.
			The optimized prompt should effectively guide the AI to understand the task and generate responses that meet the requirements.
			During the optimization process, please ensure the following key points are included:
			Task Objective: Clearly state the core task and expected outcome of the prompt.
			Output Format: Specify the required output format (e.g., list, paragraph, dialogue) to ensure the AI generates structured content.
			Key Points: List the critical elements or necessary information for the task to ensure the content is complete and relevant.

			<TASKS>
			Here is the task description (between <TASK_DESCRIPTIONS> and </TASK_DESCRIPTIONS>):
			<TASK_DESCRIPTIONS> {{TASK_DESCRIPTION}} </TASK_DESCRIPTIONS>

			Ensure the report is concise, comprehensive, and logically organized.
			After completing the optimization, only output the optimized prompt.
			And answer according to the language of the task description.
			""";

	String PROLOGUE_GENERATE_TEMPLATE = """
			<INSTRUCTIONS>
			Step 1: Identify the purpose of the chatbot from the variable {{TASK_DESCRIPTION}} and infer chatbot's tone  (e.g., friendly, professional, etc.) to add personality traits.
			Step 2: Create a coherent and engaging opening statement.
			Step 3: Ensure the output is welcoming and clearly explains what the chatbot is designed to do. Do not include any XML tags in the output.
			Example Input:
			Provide customer support for an e-commerce website
			Example Output:
			Welcome! I'm here to assist you with any questions or issues you might have with your shopping experience. Whether you're looking for product information, need help with your order, or have any other inquiries, feel free to ask. I'm friendly, helpful, and ready to support you in any way I can.
			<TASKS>
			Here is the task description: {{INPUT_TEXT}}

			You just need to generate the output.
			use the same language as the task description.
			""";

	String EXTRACT_PROMPT_TEMPLATE = """
			Extract information about a prompt from {{INPUT_TEXT}}""";

	String SUGGESTED_PROMPT_TEMPLATE = """
			{{CHAT_HISTORIES}}
			Please help me predict the three most likely questions that human would ask,
			and keeping each question under 20 characters.
			MAKE SURE your output is the SAME language as the Assistant's latest response
			Extract information about questions from output.
			questions:
			""";

	String MIND_MAP_PROMPT_TEMPLATE = """
			To create a response in a mind map format, organize the answer around the central question or topic.    Follow these structured steps:

			Central Topic: Place the main question or topic at the center.
			Primary Branches: Develop major categories or main points branching out from the central topic.    Each branch should represent a major aspect or key point related to the question.
			Sub-Branches: Add sub-branches to each primary branch.    Each sub-branch should break down the major points into further details or examples.
			Connections and Relationships: Use lines, arrows, or labels to show connections or logical flows between ideas.
			MAKE SURE your output is the SAME language as the Assistant's latest response""";

	String PPT_OUTLINE_PROMPT = """
			Based on the provided topic, generate a PowerPoint outline in JSON format suitable for presentation. It is required to maintain consistency in the language of the topic, ensure clear content structure and rigorous logic, including main titles, subtitles, multiple chapters, and their sub-chapters.

			## Input Variables
			- **Provided Topic**: {{topic}}

			## Task
			- Design an outline for a PowerPoint presentation on the topic "{{topic}}".
			- The outline includes the following parts:
			  - **Title** (`title`): Clearly state the core idea of the speech.
			  - **Subtitle** (`subTitle`): Provides supplementary information about the topic or leads to a more specific topic.
			  - **chapters** (`chapters`): Divide the content of the speech into several main sections, each of which should have its own chapter title (`chapterTitle`) and sub-chapter title (`chapterContents`) list to explain the key points under that section.
			  Each chapter and its sub-chapters must be closely focused on "{{topic}}", ensuring information relevance and coherence.
			- Facilitate the audience's understanding and the speaker's explanation.

			## Output Format
			The output is in the following JSON format:
			```json
			{
			  "title": "<Topic Name>",
			  "subTitle": "<Subtitle>",
			  "chapters": [
			    {
			      "chapterTitle": "<Chapter Title 1>",
			      "chapterContents": [
			        "<Sub chapter Title 1>",
			        "<Sub chapter Title 2>",
			        "<Sub chapter Title 3>"
			      ]
			    },
			    {
			      "chapterTitle": "<Chapter Title 2>",
			      "chapterContents": [
			        "<Sub chapter Title 1>",
			        "<Sub chapter Title 2>"
			      ]
			    }
			  ]
			}
			```

			# Output step
			1. **Understand the topic**: Deeply understand the main points and related information of "{{topic}}".
			2. **Organizational content**: Determine the appropriate chapter division and its subtitles.
			3. **Structural logic**: Ensure the logical arrangement of chapters and sub-chapters.
			4. **Generate JSON**: Output JSON content according to the specified format.
			""";

	String PPT_SLIDE_PROMPT = """
			Gain a deep understanding of the topic, outline, and chapter of the PPT, and enrich and refine the slide content with a focus on the chapter. Ensure that the content closely revolves around the chapter and maintains consistency in thematic language. Please adhere to the following requirements:

			## Task Requirements
			- **Clear Logic**: The slide content should be closely tied to the chapter, with clear organization and logical coherence.
			- **Conciseness and Clarity**: Each point should be concise and to the point, avoiding lengthy descriptions.
			- **Hierarchical Structure**: Each main point should include at least 2-3 specific sub-points, making the content both concise and rich.
			- **Consistency**: The slide content must align with the topic and outline, ensuring that the information points match the content in the outline.
			- **Visual Aids**: Provide image keywords related to the content to facilitate the addition of appropriate visual elements to the slides.

			## Input Variables
			- **Topic**: {{topic}}
			- **Outline**: {{outline}}
			- **Chapter**: {{chapter}}

			## Output Format
			The output is in the following JSON format:
			```json
			{
			  "title": "<Chapter>",
			  "image_keywords": "<Some keywords for this slide>",
			  "content": [
			    {
			      "main_point": "<Main Point 1>",
			      "sub_points": [
			        "<Sub Point 1.1>",
			        "<Sub Point 1.2>",
			        "<Sub Point 1.3>"
			      ]
			    },
			    {
			      "main_point": "<Main Point 2>",
			      "sub_points": [
			        "<Sub Point 2.1>",
			        "<Sub Point 2.2>",
			        "<Sub Point 2.3>"
			      ]
			    }
			  ]
			}
			```
			 """;

	String CHINESE_ENGLISH_TRANSLATOR_PROMPT = """
			# Instruction
			Translate the provided input text to {{language}}.

			# Output Format
			- Keep the same formatting of the input text, including punctuation and line breaks.
			- The translated text should maintain the original formatting, with similar punctuation and layout.
			""";

}
