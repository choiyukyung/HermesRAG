import { BrowserRouter, Route, Routes } from "react-router-dom";

import Home from "./Home";
import Summary from "./Summary";

function App() {

    return (
        <BrowserRouter>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/Summary" element={<Summary />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
