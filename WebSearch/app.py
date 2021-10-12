from flask import Flask, request, jsonify
import WebSearch
app = Flask(__name__)


@app.route('/searchweb')
def get():
    query = request.args.get('q')
    return jsonify(WebSearch.find_results(query))

if __name__ == "__main__":
    app.run(debug = True)