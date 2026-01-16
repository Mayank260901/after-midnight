import { Github, Twitter, Instagram } from "lucide-react";

export function Footer() {
    return (
        <footer className="w-full border-t border-white/5 bg-black/20 backdrop-blur-xl mt-auto">
            <div className="max-w-6xl mx-auto px-8 py-12">
                <div className="grid grid-cols-1 md:grid-cols-4 gap-12 mb-12">
                    <div className="md:col-span-2 space-y-4">
                        <h4 className="font-serif text-xl font-bold text-white">After Midnight</h4>
                        <p className="text-white/40 text-sm max-w-xs leading-relaxed">
                            A sanctuary for your localized thoughts, poems, and melodies.
                            Where silence speaks louder than words.
                        </p>
                    </div>

                    <div className="space-y-4">
                        <h4 className="text-sm font-medium text-white/80 uppercase tracking-widest">Explore</h4>
                        <ul className="space-y-2 text-sm text-white/50">
                            <li><a href="/poems" className="hover:text-white transition-colors">Poems</a></li>
                            <li><a href="/thoughts" className="hover:text-white transition-colors">Thoughts</a></li>
                            <li><a href="/songs" className="hover:text-white transition-colors">Music</a></li>
                        </ul>
                    </div>

                    <div className="space-y-4">
                        <h4 className="text-sm font-medium text-white/80 uppercase tracking-widest">Connect</h4>
                        <div className="flex gap-4 text-white/50">
                            <a href="#" className="hover:text-white transition-colors"><Twitter size={20} /></a>
                            <a href="#" className="hover:text-white transition-colors"><Instagram size={20} /></a>
                            <a href="#" className="hover:text-white transition-colors"><Github size={20} /></a>
                        </div>
                    </div>
                </div>

                <div className="border-t border-white/5 pt-8 flex flex-col md:flex-row justify-between items-center text-xs text-white/30">
                    <p>&copy; {new Date().getFullYear()} After Midnight. All rights reserved.</p>
                    <div className="flex gap-6 mt-4 md:mt-0">
                        <a href="#" className="hover:text-white transition-colors">Privacy</a>
                        <a href="#" className="hover:text-white transition-colors">Terms</a>
                    </div>
                </div>
            </div>
        </footer>
    );
}
