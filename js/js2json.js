const input = process.env.JS2JSON_INPUT
if (!input) {
    throw new Error('js2json.js requires the "JS2JSON_INPUT" env var to be set.')
}
console.log(JSON.stringify(require(`../${input}`), null, 4))