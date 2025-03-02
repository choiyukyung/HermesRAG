import google.generativeai as genai
import os, json

#API_KEY 환경 변수로 저장
API_KEY = os.getenv('GOOGLE_API_KEY')

genai.configure(api_key=API_KEY)
model = genai.GenerativeModel('gemini-2.0-flash-lite')

prompt = """
who are you?
Response in JSON format.
"""

response = model.generate_content(
    prompt,
    generation_config={
        "response_mime_type": "application/json"
    }
)

response_json = json.loads(response.text)
print(response_json)