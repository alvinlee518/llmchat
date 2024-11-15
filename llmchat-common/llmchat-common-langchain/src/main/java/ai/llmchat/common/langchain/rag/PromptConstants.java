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
}
