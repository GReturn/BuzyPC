package io.buzypc.app.AI

import android.util.Log
import com.azure.ai.inference.ChatCompletionsClientBuilder
import com.azure.ai.inference.models.ChatCompletions
import com.azure.ai.inference.models.ChatCompletionsOptions
import com.azure.ai.inference.models.ChatRequestMessage
import com.azure.ai.inference.models.ChatRequestSystemMessage
import com.azure.ai.inference.models.ChatRequestUserMessage
import com.azure.core.credential.AzureKeyCredential
import io.buzypc.app.Dependencies.Environment.Configuration

//import com.azure.core.util.Configuration

class BuzyAI {
    // Learn how to use the Azure AI Inference library with GPT-4o: https://github.com/marketplace/models/azure-openai/gpt-4o

//    private val key: String = Configuration.getGlobalConfiguration().get("GITHUB_TOKEN")
    private val key = Configuration.get("GITHUB_TOKEN")
    private val endpoint = "https://models.inference.ai.azure.com"
    private val model = "gpt-4o"

    private val client = ChatCompletionsClientBuilder()
        .credential(AzureKeyCredential(key))
        .endpoint(endpoint)
        .buildClient()

    /**
     * Generates an XML representation of a PC build based on the provided build name and budget.
     *
     * This function interacts with a Language Model (LLM) to generate a detailed PC build in XML format.
     * It constructs a query for the LLM based on the `buildName` and `budget`, then uses the LLM's response
     * to create the XML output. The response from the LLM is assumed to be in a valid XML format.
     *
     * @param buildName The desired name or theme for the PC build (e.g., "Gaming Rig", "Workstation", "Budget Build").
     *                  This string is used to guide the LLM in generating a relevant build.
     * @param budget    The maximum budget for the PC build. This value is used to constrain the LLM's choices
     *                  of components and ensure the build falls within the specified price range.
     * @return          A string containing the XML representation of the PC build.
     *                  This string is the raw response received from the LLM.
     * @throws Exception if there are issues with communicating with the LLM or processing its response.
     *
     * @sample
     * ```kotlin
     *  val buildName = "Gaming PC"
     *  val budget = 1500.0
     *  val pcBuildXML = generatePCBuildXML(buildName, budget)
     *  println(pcBuildXML)
     *  ```
     */
    fun generatePCBuildXML(buildName: String, budget: Double): String {
        val message = generateLLMQuery(buildName, budget)
        return createCompletion(message).choices[0].message.content
    }

    /**
     * Creates a chat completion using the Azure OpenAI service.
     *
     * This function takes a list of chat messages as input, configures the completion options
     * with the specified model, and then uses the OpenAI client to generate a chat completion.
     *
     * @param message A list of [ChatRequestMessage] objects representing the conversation history.
     *                This list should contain messages from both the user and the assistant.
     *                The order of the messages in the list is significant, as it represents the conversation flow.
     * @return A [ChatCompletions] object containing the generated completion response from the Azure OpenAI service.
     * @throws com.azure.core.exception.HttpResponseException if the request to the Azure OpenAI service fails.
     * @throws NullPointerException if the `client` or `model` in this class is null.
     *
     * Example Usage:
     * ```kotlin
     *  val messages = listOf(
     *      ChatRequestUserMessage("What is the capital of France?"),
     *      ChatRequestAssistantMessage("The capital of France is Paris."),
     *      ChatRequestUserMessage("What is the population of Paris?")
     *    )
     *  val completion = createCompletion(messages)
     *  println(completion.choices.first().message.content)
     * ```
     */
    private fun createCompletion(message: List<ChatRequestMessage>): ChatCompletions {
        val chatCompletionsOptions = ChatCompletionsOptions(message)
        chatCompletionsOptions.model = model
        return client.complete(chatCompletionsOptions)
    }

    /**
     * Generates a list of chat messages to query a Language Learning Model (LLM) for a PC build suggestion.
     *
     * This function constructs a prompt for an LLM, providing context and instructions to generate a PC build
     * recommendation based on a specified budget and build name. The LLM is instructed to act as a computer
     * specialist in Cebu City, Philippines, and to format its response as an XML document with a specific
     * structure.
     *
     * @param buildName The name of the PC build (e.g., "Gaming Rig", "Workstation"). This will be included
     *                  in the generated XML under the <BuildName> tag.
     * @param budget The budget allocated for the PC build in Philippine Pesos. This value is provided to the
     *               LLM to help it choose appropriate components.
     * @return A list containing two `ChatRequestMessage` objects:
     *         1. A `ChatRequestSystemMessage` setting the context of the conversation (i.e., the LLM's role).
     *         2. A `ChatRequestUserMessage` containing the detailed prompt for the LLM, including the budget,
     *            instructions for XML format, and the desired XML structure.
     *     */
    private fun generateLLMQuery(buildName: String, budget: Double): List<ChatRequestMessage> {
        Log.d("BuzyAI", "Generating PC build for $buildName with budget $budget")
        return listOf(
            ChatRequestSystemMessage("You are computer specialist in Cebu City, Philippines."),
            ChatRequestUserMessage("You are given a budget in Philippine Pesos: ${budget}\\n" +
                    "\\n" +
                    "Do not add any paragraphs. Without inserting a new text for BuildName " +
                    "and maximizing on the given budget with 2-3 stores per component, " +
                    "do not go over the budget, " +
                    "generate a PC Build in XML " +
                    "(no need to put it in a code block. I just need a text) with the following Object structure:\\n" +
                    "\\n" +
                    "<PC>\n" +
                    "    <MotherboardComponent>\n" +
                    "        <Name type=\"string\">...</Name>\n" +
                    "        <Brand type=\"string\">...</Brand>\n" +
                    "        <Price type=\"double\" unit=\"PHP\">...</Price>\n" +
                    "        <PerformanceScore type=\"float\" range=\"0.0-10.0\">...</PerformanceScore>\n" +
                    "        <CompatibilityScore type=\"float\" range=\"0.0-10.0\">...</CompatibilityScore>\n" +
                    "        <IsBought type=\"boolean\">false</IsBought>\n" +
                    "        <Stores>\n" +
                    "            <Store>\n" +
                    "                <StoreName type=\"string\">...</Name>\n" +
                    "                <VendorSite type=\"string\" format=\"url\">...</VendorSite>\n" +
                    "                <PriceRange>\n" +
                    "                    <Min type=\"double\" unit=\"USD\">...</Min>\n" +
                    "                    <Max type=\"double\" unit=\"USD\">...</Max>\n" +
                    "                </PriceRange>\n" +
                    "            </Store>\n" +
                    "            <!-- More Store entries -->\n" +
                    "        </Stores>\n" +
                    "    </MotherboardComponent>\n" +
                    "\n" +
                    "    <!-- Repeat similar structure for each component -->\n" +
                    "\n" +
                    "    <CPUComponent>\n" +
                    "        <!-- Same structure as MotherboardComponent -->\n" +
                    "    </CPUComponent>\n" +
                    "\n" +
                    "    <GPUComponent>\n" +
                    "        <!-- Same structure -->\n" +
                    "    </GPUComponent>\n" +
                    "\n" +
                    "    <StorageDeviceComponent>\n" +
                    "        <!-- Same structure -->\n" +
                    "    </StorageDeviceComponent>\n" +
                    "\n" +
                    "    <RAMComponent>\n" +
                    "        <!-- Same structure -->\n" +
                    "    </RAMComponent>\n" +
                    "\n" +
                    "    <PSUComponent>\n" +
                    "        <!-- Same structure -->\n" +
                    "    </PSUComponent>\n" +
                    "</PC>")
        )
    }
}

