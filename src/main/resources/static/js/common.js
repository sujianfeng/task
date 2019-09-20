/**
 * 校验是否为json数据
 * @param jsonContent
 * @returns {boolean}
 */
function isJSON(jsonContent) {
    if (typeof jsonContent == 'string') {
        try {
            var obj = JSON.parse(jsonContent);
            if (jsonContent.indexOf('{') > -1) {
                return true;
            } else {
                return false;
            }

        } catch (e) {
            console.log(e);
            return false;
        }
    }
    return false;
}

/**
 * 格式化json文本
 * @param jsonContent
 * @returns {string}
 */
function formatJSON(jsonContent) {
    if (!jsonContent) {
        return "";
    } else {
        var obj = JSON.parse(jsonContent);
        var formattedStr = JSON.stringify(obj, null, 0);
        return formattedStr;
    }
}