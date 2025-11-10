import React, { useState, useRef } from 'react';
import { Music2, Music3 } from 'lucide-react';

interface SliderRatingProps {
  initialRating?: number;
  onRatingChange?: (rating: number) => void;
}

const RatingComp: React.FC<SliderRatingProps> = ({ 
  initialRating = 0, 
  onRatingChange 
}) => {
  const [rating, setRating] = useState<number>(initialRating);
  const [isDragging, setIsDragging] = useState<boolean>(false);
  const sliderRef = useRef<HTMLDivElement>(null);

  const maxRating = 5;
  const step = 0.5;

  const updateRating = (clientX: number): void => {
    if (!sliderRef.current) return;

    const rect = sliderRef.current.getBoundingClientRect();
    const x = clientX - rect.left;
    const percentage = Math.max(0, Math.min(1, x / rect.width));
    
    // Calculate rating in 0.5 increments
    const newRating = Math.round((percentage * maxRating) / step) * step;
    const clampedRating = Math.max(0, Math.min(maxRating, newRating));
    
    setRating(clampedRating);
    onRatingChange?.(clampedRating);
  };

  const handleMouseDown = (e: React.MouseEvent): void => {
    setIsDragging(true);
    updateRating(e.clientX);
  };

  const handleMouseMove = (e: MouseEvent): void => {
    if (isDragging) {
      updateRating(e.clientX);
    }
  };

  const handleMouseUp = (): void => {
    setIsDragging(false);
  };

  React.useEffect(() => {
    if (isDragging) {
      window.addEventListener('mousemove', handleMouseMove);
      window.addEventListener('mouseup', handleMouseUp);
      
      return () => {
        window.removeEventListener('mousemove', handleMouseMove);
        window.removeEventListener('mouseup', handleMouseUp);
      };
    }
  }, [isDragging]);

  const percentage = (rating / maxRating) * 100;

  return (
      <div className="rounded-2xl w-full max-w-md p-2 md:p-0">
        
        <div className="mb-2">
            <div className="flex justify-between mb-2 px-0.5">
            {[...Array(11)].map((_, i) => {
              const value = i * 0.5;
              const isWhole = value % 1 === 0;
              return (
                <span
                  key={i}
                  className={`text-xs font-medium transition-colors ${
                    rating >= value ? 'text-yellow-400' : 'text-white/40'
                  }`}
                >
                  {isWhole ? value : '½'}
                </span>
              );
            })}
            </div>
          {/* Slider track */}          
          <div
            ref={sliderRef}
            className="relative h-3 bg-white/20 rounded-full cursor-pointer overflow-hidden"
            onMouseDown={handleMouseDown}
          >
            {/* Filled portion */}
            <div
              className="absolute top-0 left-0 h-full bg-gradient-to-r from-yellow-400 to-orange-400 rounded-full transition-all duration-100 shadow-[0_0_20px_rgba(250,204,21,0.4)]"
              style={{ width: `${percentage}%` }}
            />
            
            {/* Tick marks */}
            <div className="absolute inset-0 flex justify-between px-1">
              {[...Array(11)].map((_, i) => (
                <div
                  key={i}
                  className="w-0.5 h-full bg-white/30"
                />
              ))}
            </div>
          </div>

          {/* Music note indicators */}
          <div className="flex gap-1 mt-4 justify-center flex-wrap">
            {[...Array(Math.floor(rating))].map((_) => (
              <Music3 className="sm:w-4 sm:h-4 md:w-8 md:h-8" color='rgba(233, 218, 16, 1)'/>
            ))}
            {rating % 1 === 0.5 && (
              <Music2 className="sm:w-4 sm:h-4 md:w-8 md:h-8" color='rgba(233, 218, 16, 1)' />
            )}
          </div>
        </div>

        {rating > 0 && (
          <button
            onClick={() => {
              setRating(0);
              onRatingChange?.(0);
            }}
            className="w-full px-4 py-2 my-2 bg-white/10 hover:bg-white/20 text-white rounded-lg transition-colors duration-200 border border-white/20"
          >
            Clear Rating
          </button>
        )}
      </div>
  );
};

export default RatingComp;