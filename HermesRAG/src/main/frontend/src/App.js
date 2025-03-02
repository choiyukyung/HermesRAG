import { BrowserRouter, Route, Routes } from "react-router-dom";

import Home from "./Home";
import Summary from "./Summary";
import Answer from "./Answer";

function App() {

    return (
        <BrowserRouter>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/Summary" element={<Summary />} />
                    <Route path="/Answer" element={<Answer />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
