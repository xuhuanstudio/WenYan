package compile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CodeLoader {

    private boolean isDebug = false;

    private final ScriptEngine scriptEngine;

    private final boolean isEnabled;

    {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
        isEnabled = scriptEngine != null;
    }

    public void setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void invoke(String code) throws ScriptException {
        if (getIsEnabled()) {
            // 注入内置对象
            scriptEngine.put("output", new Output());
            scriptEngine.eval(code);
        } else {
            if (isDebug) {
                Output.debugPrintln("当前版本得JDK版本未能提供 WenYan 的运行条件");
            }
        }
    }

}
