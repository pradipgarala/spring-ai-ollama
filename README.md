Spring AI Multimedia (with OpenAI GPT-4o)
===

source from : https://github.com/habuma/spring-ai-examples/tree/main/spring-ai-multimodal

This is a simple demonstration of how to use Spring AI with ollama > llava model to answer questions about an image. The image provided
is a weather forecast image in the project at
`src/main/resources/static/forecast.jpg`.

Once the application starts up, you can begin asking questions by 
POSTing to the `/ask` endpoint. The body of the POST request should be
a simple JSON document with a "question" property.

For example, here's how you might ask a question using the `curl` 
command line tool:

~~~
curl localhost:8080/ask -H"Content-type: application/json" -d'{"question":"What would be a good day to wash my car?"}'
  
curl localhost:8080/ask -H"Content-type: application/json" -d'{"question":"What do you see?"}'
~~~

Or, if using HTTPie:

~~~
http :8080/ask question="What would be a good day to wash my car?"
~~~

