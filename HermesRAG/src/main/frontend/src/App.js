import { BrowserRouter, Route, Routes } from "react-router-dom";

import Home from "./Home";
import Rag from "./CommandSummary";

function App() {

    return (
        <BrowserRouter>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/rag" element={<Rag />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
