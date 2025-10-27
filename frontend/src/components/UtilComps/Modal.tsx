
import React, { useState, useEffect } from 'react';

interface Props {
    isOpen: boolean;
    onClose: () => void;
    children: React.ReactNode;
}

export default function Modal({isOpen, onClose, children}: Props) {
    const [showBackdrop, setShowBackdrop] = useState(false);
    const [showContent, setShowContent] = useState(false);

    useEffect(() => {
        if (isOpen) {
            setShowBackdrop(true);

            const timer = setTimeout(() => {
                setShowContent(true);
            }, 200);

            return () => clearTimeout(timer);
        } else {
            setShowContent(false);
            setShowBackdrop(false);
        }
    }, [isOpen]);

    if(!isOpen) return null;

    return (
        <div
        className={`fixed inset-0 z-50 flex items-center justify-center p-4 transition-all duration-200 ${
            showBackdrop ? 'backdrop-blur-md bg-black/50' : 'backdrop-blur-none bg-black/0'
        }`}
        onClick={onClose}
        >
        <div
            className={`flex bg-gradient-to-r from-purple-500 via-pink-500 to-blue-500 rounded-lg shadow-2xl p-4 sm:p-6 md:p-8 w-full max-w-6xl max-h-148 transition-all duration-300 ${
                showContent
                ? 'scale-100 opacity-100'
                : 'scale-75 opacity-0'
            }`}
            onClick={(e) => e.stopPropagation()}
        >
            {children}
        </div>
        </div>
    );
}