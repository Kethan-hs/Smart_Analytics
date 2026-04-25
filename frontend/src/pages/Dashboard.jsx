import React, { useState } from 'react';
import { apiClient } from '../api/client';
import UploadZone from '../components/UploadZone';
import LoadingSpinner from '../components/LoadingSpinner';
import InsightGrid from '../components/InsightGrid';
import DataChart from '../components/DataChart';

const Dashboard = () => {
  const [isUploading, setIsUploading] = useState(false);
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [insightData, setInsightData] = useState(null);
  const [error, setError] = useState(null);

  const handleFileUpload = async (file) => {
    setIsUploading(true);
    setError(null);
    setInsightData(null);

    const formData = new FormData();
    formData.append('file', file);

    try {
      const uploadRes = await apiClient.post('/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      
      const datasetId = uploadRes.data.datasetId;
      
      setIsUploading(false);
      setIsAnalyzing(true);

      const analyzeRes = await apiClient.post('/analyze', { datasetId });
      setInsightData(analyzeRes.data);
      
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'An error occurred');
    } finally {
      setIsUploading(false);
      setIsAnalyzing(false);
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-6 md:p-8">
      <header className="mb-10">
        <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight mb-2">SmartInsight</h1>
        <p className="text-gray-500">AI-Powered Analytics Dashboard</p>
      </header>

      <main className="space-y-8">
        {!insightData && !isAnalyzing && (
          <section className="max-w-2xl">
            <UploadZone onUpload={handleFileUpload} isUploading={isUploading} />
            {error && (
              <div className="mt-4 p-4 bg-red-50 text-red-700 rounded-lg border border-red-100">
                {error}
              </div>
            )}
          </section>
        )}

        {(isUploading || isAnalyzing) && (
          <LoadingSpinner message={isUploading ? "Uploading dataset..." : "AI is analyzing your data..."} />
        )}

        {insightData && !isAnalyzing && (
          <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-bold text-gray-900">Analysis Results</h2>
              <button 
                onClick={() => setInsightData(null)}
                className="text-sm font-medium text-blue-600 hover:text-blue-800"
              >
                Upload New Dataset
              </button>
            </div>
            
            <InsightGrid insightsData={insightData} />
            <DataChart />
          </div>
        )}
      </main>
    </div>
  );
};

export default Dashboard;
