document.addEventListener("DOMContentLoaded", () => {
    const chatBody = document.querySelector(".chat-body");
    const messageInput = document.querySelector(".message-input");
    const sendMessageButton = document.querySelector("#send-message");
    const chatbotToggler = document.querySelector("#chatbot-toggler");
    const scrollBar = document.querySelector('.chat-form .message-input');

        //  Added function to convert bullets to numbered list
    const convertBulletsToNumbers = (text) => {
        let counter = 1;
        return text
            .split('\n')
            .map(line => {
                if (/^\*\s+/.test(line)) {
                    return line.replace(/^\*\s+/, `${counter++}. `);
                }
                return line;
            })
            .join('\n');
    };
    
    const isBankingRelated = (message) => {
        const bankingKeywords = [
            "account", "balance", "transfer", "deposit", "withdrawal", "loan", 
            "credit", "debit", "interest", "mortgage", "investment", "bank", 
            "card", "statement", "payment", "transaction", "savings", "checking",
            "currency", "exchange", "ATM", "IBAN", "SWIFT", "wire transfer", "overdraft",
            "password", "forgot password", "login", "change password", "hello", "hi", "no", "yes",
            "ok", "thank you", "lek", "euro", "dollar", "them", "for", "each", "of"
        ];
        const lowercasedMessage = message.toLowerCase();
        return bankingKeywords.some(keyword => lowercasedMessage.includes(keyword));
    };

        let knowledgeBase = "";

    // Fetch knowledge base text
    fetch("/knowledge_base.txt")
        .then(response => response.text())
        .then(text => {
            knowledgeBase = text;
        });

    // API setup
    const API_KEY = "";
    const API_URL = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${API_KEY}`;

    const userData = {
        message: null
    };

    const chatHistory = [];
    const initialInputHeight = messageInput.scrollHeight;

    const createMessageElement = (content, ...classes) => {
        const div = document.createElement("div");
        div.classList.add("message", ...classes);
        div.innerHTML = content;
        return div;
    };

    // Normalize informal currency terms
    const normalizeCurrency = (currency) => {
        const currencyAliases = {
            euro: "EUR", euros: "EUR", eur: "EUR",
            dollar: "USD", dollars: "USD", usd: "USD", bucks: "USD",
            pound: "GBP", pounds: "GBP", gbp: "GBP",
            yen: "JPY", jpy: "JPY",
            rupee: "INR", rupees: "INR", inr: "INR",
            lek: "ALL"
        };
        return currencyAliases[currency.toLowerCase()] || currency.toUpperCase();
    };

    // Currency converter
    const convertCurrency = async (amount, fromCurrency, toCurrency) => {
        const API_KEY = "";
        const response = await fetch(`https://v6.exchangerate-api.com/v6/${API_KEY}/pair/${fromCurrency}/${toCurrency}/${amount}`);
        const data = await response.json();
        if (data.result === "success") {
            return `${amount} ${fromCurrency} = ${data.conversion_result} ${toCurrency}`;
        } else {
            throw new Error("Currency conversion failed.");
        }
    };
    
        const getRelevantKnowledge = (question) => {
        const sentences = knowledgeBase.split('.');
        return sentences.filter(sentence =>
            question.toLowerCase().split(' ').some(word => sentence.toLowerCase().includes(word))
        ).join('. ') + '.';
    };

    // Generate bot response
    const generateBotResponse = async (incomingMessageDiv) => {
        const messageElement = incomingMessageDiv.querySelector(".message-text");
        const userMessage = userData.message;
        
const transferRegex = /(?:transfer|send|move)\s+(-?\d+(?:\.\d+)?)\s*(\w+)?\s*(?:from)?\s*(AL\d{13})\s*(?:to)?\s*(AL\d{13})(?:\s*(?:for|to|with)\s*([\w\s]+))?/i;
const transferMatch = userData.message.match(transferRegex);

if (transferMatch) {
    const [, amount, currencyRaw, senderAccount, receiverAccount, nameOrDescription] = transferMatch;
    const currency = normalizeCurrency(currencyRaw || "EUR");
    
    let name = null;
    let description = null;

    if (nameOrDescription) {
        const nameDescSplit = nameOrDescription.split(/\s+for\s+|\s+to\s+|\s+with\s+/i);
        if (nameDescSplit.length > 1) {
            name = nameDescSplit[0].trim();
            description = nameDescSplit[1].trim();
        } else {
            // Treat single value as description, not name
            description = nameOrDescription.trim();
            name = null;
        }
    }

    try {
        const response = await fetch("/api/transaction/transfer", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                accountNumber: senderAccount,
                accountNumberBeneficiary: receiverAccount,
                amount: parseFloat(amount),
                currency: currency,
                name: name,
                description: description
            })
        });

        const result = await response.text();

        if (!response.ok) {
            messageElement.innerText = result;
            messageElement.style.color = "#ff0000";
        } else {
            messageElement.innerText = `Transaction successful.\n\nDo you want to make another transaction?`;
        }

        chatHistory.push({ role: "model", parts: [{ text: messageElement.innerText }] });
    } catch (error) {
        messageElement.innerText = "Transaction error: " + error.message;
        messageElement.style.color = "#ff0000";
        chatHistory.push({ role: "model", parts: [{ text: messageElement.innerText }] });
    } finally {
        incomingMessageDiv.classList.remove("thinking");
        chatBody.scrollTo({ top: chatBody.scrollHeight, behavior: "smooth" });  
    }
    return;
}


        // Check for balance request with account number
        const balanceRegex = /(?:balance|show balance|check balance)\s*(?:of|for)?\s*(AL\d{13})/i;
        const balanceMatch = userData.message.match(balanceRegex);

        if (balanceMatch) {
            const accountNumber = balanceMatch[1];

            try {
                const response = await fetch(`/api/account/balance?accountNumber=${accountNumber}`);
                if (!response.ok) {
                    const errorText = await response.text();
                    messageElement.innerText = errorText;
                    messageElement.style.color = "#ff0000";
                } else {
                    const balanceData = await response.json();
                    const balance = balanceData.balance;
                    const currency = balanceData.currency;

                    const message = `The balance for account ${accountNumber} is ${balance} ${currency}.`;

                    messageElement.innerText = message;

                    chatHistory.push({
                        role: "model",
                        parts: [{ text: message }]
                    });
                }
            } catch (error) {
                messageElement.innerText = "Failed to retrieve balance.";
                messageElement.style.color = "#ff0000";
            } finally {
                incomingMessageDiv.classList.remove("thinking");
                chatBody.scrollTo({ top: chatBody.scrollHeight, behavior: "smooth" });
            }
            return;
        }

        // Currency conversion logic
        const currencyRegex = /\b(?:convert|change|exchange)\b\s+(\d+(?:\.\d+)?)\s*(\w+)\s+(?:to|in|into|for)\s+(\w+)/i;
        const currencyMatch = userData.message.match(currencyRegex);
        if (currencyMatch) {
            const [, amount, fromCurrencyRaw, toCurrencyRaw] = currencyMatch;
            const fromCurrency = normalizeCurrency(fromCurrencyRaw);
            const toCurrency = normalizeCurrency(toCurrencyRaw);

            try {
                const result = await convertCurrency(parseFloat(amount), fromCurrency, toCurrency);
                const message = `Sure! ${result}.\n\nDo you want to convert another amount?`;
                messageElement.innerText = message;

                chatHistory.push({
                    role: "model",
                    parts: [{ text: message }]
                });
            } catch (error) {
                messageElement.innerText = error.message;
                messageElement.style.color = "#ff0000";
            } finally {
                incomingMessageDiv.classList.remove("thinking");
                chatBody.scrollTo({ top: chatBody.scrollHeight, behavior: "smooth" });
            }
            return;
        }

        // Banking-related message check
        if (!isBankingRelated(userData.message)) {
            const warning = "Sorry, I can only assist with banking-related topics.";
            messageElement.innerText = warning;
            messageElement.style.color = "#ff0000";

            chatHistory.push({
                role: "model",
                parts: [{ text: warning }]
            });

            incomingMessageDiv.classList.remove("thinking");
            chatBody.scrollTo({ top: chatBody.scrollHeight, behavior: "smooth" });
            return;
        }

        // Fallback - intelligent response using knowledge base
        const relevantKnowledge = getRelevantKnowledge(userMessage);

        chatHistory.push({
            role: "user",
            parts: [{
                text: `Here is some information that might help:\n${relevantKnowledge}\n\nUser's question:\n${userMessage}\n\nPlease answer in a clear, friendly, and natural way.`            
            }]
        }); 

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ contents: chatHistory })
            });

            const data = await response.json();
            if (!response.ok) throw new Error(data.error.message);

            const apiResponseText = data.candidates[0].content.parts[0].text.replace(/\*\*(.*?)\*\*/g, "$1").trim();
            messageElement.innerText = apiResponseText;
            
             // Convert any bullet points into numbered format
            const numberedText = convertBulletsToNumbers(apiResponseText);

            messageElement.innerText = numberedText;

        } catch (error) {
            messageElement.innerText = error.message;
            messageElement.style.color = "#ff0000";
        } finally {
            incomingMessageDiv.classList.remove("thinking");
            chatBody.scrollTo({ top: chatBody.scrollHeight, behavior: "smooth" });
        }
    };

    // Handle user message
    const handleOutgoingMessage = (e) => {
        e.preventDefault();
        userData.message = messageInput.value.trim();
        messageInput.value = "";
        messageInput.dispatchEvent(new Event("input"));

        const messageContent = `<div class="message-text"></div>`;
        const outgoingMessageDiv = createMessageElement(messageContent, "user-message");
        outgoingMessageDiv.querySelector(".message-text").textContent = userData.message;
        chatBody.appendChild(outgoingMessageDiv);
        chatBody.scrollTo({ top: chatBody.scrollHeight, behavior: "smooth" });

        // Bot thinking bubble
        setTimeout(() => {
            const messageContent = `<svg class="bot-avatar" xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 1024 1024">
                                <path d="M738.3 287.6H285.7c-59 0-106.8 47.8-106.8 106.8v303.1c0 59 47.8 106.8 106.8 106.8h81.5v111.1c0 .7.8 1.1 1.4.7l166.9-110.6 41.8-.8h117.4l43.6-.4c59 0 106.8-47.8 106.8-106.8V394.5c0-59-47.8-106.9-106.8-106.9zM351.7 448.2c0-29.5 23.9-53.5 53.5-53.5s53.5 23.9 53.5 53.5-23.9 53.5-53.5 53.5-53.5-23.9-53.5-53.5zm157.9 267.1c-67.8 0-123.8-47.5-132.3-109h264.6c-8.6 61.5-64.5 109-132.3 109zm110-213.7c-29.5 0-53.5-23.9-53.5-53.5s23.9-53.5 53.5-53.5 53.5 23.9 53.5 53.5-23.9 53.5-53.5 53.5zM867.2 644.5V453.1h26.5c19.4 0 35.1 15.7 35.1 35.1v121.1c0 19.4-15.7 35.1-35.1 35.1h-26.5zM95.2 609.4V488.2c0-19.4 15.7-35.1 35.1-35.1h26.5v191.3h-26.5c-19.4 0-35.1-15.7-35.1-35.1zM561.5 149.6c0 23.4-15.6 43.3-36.9 49.7v44.9h-30v-44.9c-21.4-6.5-36.9-26.3-36.9-49.7 0-28.6 23.3-51.9 51.9-51.9s51.9 23.3 51.9 51.9z"></path>
                                </svg>
                                <div class="message-text">
                                    <div class="thinking-indicator">  
                                        <div class="dot"></div>
                                        <div class="dot"></div>
                                        <div class="dot"></div>
                                    </div> 
                                </div>`;
            const incomingMessageDiv = createMessageElement(messageContent, "bot-message", "thinking");
            chatBody.appendChild(incomingMessageDiv);
            chatBody.scrollTo({ top: chatBody.scrollHeight, behavior: "smooth" });
            generateBotResponse(incomingMessageDiv);
        }, 600);
    };

    messageInput.addEventListener("keydown", (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
            const userMessage = messageInput.value.trim();
            if (userMessage) {
                handleOutgoingMessage(e);
            }
        }
    });

    messageInput.addEventListener("input", () => {
        messageInput.style.height = `${initialInputHeight}px`;
        messageInput.style.height = `${messageInput.scrollHeight}px`;
        document.querySelector(".chat-form").style.borderRadius =
            messageInput.scrollHeight > initialInputHeight ? "15px" : "32px";
    });

    sendMessageButton.addEventListener("click", (e) => handleOutgoingMessage(e));
    chatbotToggler.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));

    messageInput.addEventListener('input', () => {
    messageInput.style.overflowY = messageInput.scrollHeight > messageInput.clientHeight ? 'auto' : 'hidden';
    });
});
