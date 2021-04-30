package projeto.algorithms_process_mining.inductive_miner;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.ProMCanceller;
import org.processmining.plugins.InductiveMiner.Function;
import org.processmining.plugins.InductiveMiner.efficienttree.UnknownTreeNodeException;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMinerPanel;
import org.processmining.plugins.inductiveVisualMiner.InductiveVisualMinerState;
import org.processmining.plugins.inductiveVisualMiner.Selection;
import org.processmining.plugins.inductiveVisualMiner.alignment.AlignmentComputer;
import org.processmining.plugins.inductiveVisualMiner.attributes.IvMVirtualAttributeFactory;
import org.processmining.plugins.inductiveVisualMiner.chain.*;
import org.processmining.plugins.inductiveVisualMiner.configuration.InductiveVisualMinerConfiguration;
import org.processmining.plugins.inductiveVisualMiner.dataanalysis.DataAnalysisTableFactory;
import org.processmining.plugins.inductiveVisualMiner.ivmfilter.highlightingfilter.HighlightingFilter;
import org.processmining.plugins.inductiveVisualMiner.ivmfilter.preminingfilters.PreMiningFilter;
import org.processmining.plugins.inductiveVisualMiner.mode.*;
import org.processmining.plugins.inductiveVisualMiner.popup.*;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.VisualMinerWrapper;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.AllOperatorsMiner;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.DfgMiner;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.LifeCycleMiner;
import org.processmining.plugins.inductiveVisualMiner.visualMinerWrapper.miners.Miner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class InductiveVisualMinerConfigurationVaam implements InductiveVisualMinerConfiguration {
    protected Cl01GatherAttributes gatherAttributes;
    protected Cl02SortEvents sortEvents;
    protected Cl03MakeLog makeLog;
    protected Cl04FilterLogOnActivities filterLogOnActivities;
    protected Cl05Mine mine;
    protected Cl06LayoutModel layoutModel;
    protected Cl07Align align;
    protected Cl08UpdateIvMAttributes ivmAttributes;
    protected Cl09LayoutAlignment layoutAlignment;
    protected Cl10AnimationScaler animationScaler;
    protected Cl11Animate animate;
    protected Cl12TraceColouring traceColouring;
    protected Cl13FilterNodeSelection filterNodeSelection;
    protected Cl14Performance performance;
    protected Cl15Histogram histogram;
    protected Cl16DataAnalysisTrace dataAnalysisTrace;
    protected Cl17DataAnalysisEvent dataAnalysisEvent;
    protected Cl18DataAnalysisCohort dataAnalysisCohort;
    protected Cl19DataAnalysisLog dataAnalysisLog;
    protected Cl20Done done;

    public InductiveVisualMinerConfigurationVaam() {

    }

    protected InductiveVisualMinerState createState(XLog log) {
        return new InductiveVisualMinerState(log);
    }

    protected List<VisualMinerWrapper> createDiscoveryTechniques() {
        return new ArrayList<>(Arrays.asList(new VisualMinerWrapper[] { //
                new Miner(), //
                new DfgMiner(), //
                new LifeCycleMiner(), //
                new AllOperatorsMiner(), //
        }));
    }

    protected List<Mode> createModes() {
        return new ArrayList<>(Arrays.asList(new Mode[] { //
                new ModePaths(), //
                new ModePathsDeviations(), //
                new ModePathsQueueLengths(), //
                new ModePathsSojourn(), //
                new ModePathsWaiting(), //
                new ModePathsService(), //
                new ModeRelativePaths() }));
    }

    public Chain<InductiveVisualMinerState> createChain(final InductiveVisualMinerState state, final InductiveVisualMinerPanel panel1, ProMCanceller canceller, Executor executor, List<PreMiningFilter> preMiningFilters, List<HighlightingFilter> highlightingFilters) {
        Chain<InductiveVisualMinerState> chain = new Chain(state, canceller, executor);
        this.gatherAttributes = new Cl01GatherAttributes();
        this.sortEvents = new Cl02SortEvents();
        this.makeLog = new Cl03MakeLog();
        this.filterLogOnActivities = new Cl04FilterLogOnActivities();
        this.mine = new Cl05Mine();
        this.layoutModel = new Cl06LayoutModel();
        this.align = new Cl07Align();
        this.ivmAttributes = new Cl08UpdateIvMAttributes();
        this.layoutAlignment = new Cl09LayoutAlignment();
        this.animationScaler = new Cl10AnimationScaler();
        this.animate = new Cl11Animate();
        this.traceColouring = new Cl12TraceColouring();
        //this.filterNodeSelection = new Cl13FilterNodeSelection();
        this.performance = new Cl14Performance();
        this.histogram = new Cl15Histogram();
        this.dataAnalysisTrace = new Cl16DataAnalysisTrace();
        this.dataAnalysisEvent = new Cl17DataAnalysisEvent();
        this.dataAnalysisCohort = new Cl18DataAnalysisCohort();
        this.dataAnalysisLog = new Cl19DataAnalysisLog();
        this.done = new Cl20Done();
        this.gatherAttributes.setOnComplete(new Runnable() {
            public void run() {
                //panel.getClassifiers().setEnabled(true);
                //panel.getClassifiers().replaceClassifiers(state.getClassifiers(), state.getInitialClassifier());
                state.getPreMiningFiltersController().setAttributesInfo(state.getAttributesInfo());
            }
        });
        this.gatherAttributes.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getClassifiers().setEnabled(false);
            }
        });
        this.sortEvents.setOnIllogicalTimeStamps(new Function<Object, Boolean>() {
            public Boolean call(Object input) throws Exception {
                String[] options = new String[]{"Continue with neither animation nor performance", "Reorder events"};
                //int n = JOptionPane.showOptionDialog(panel, "The event log contains illogical time stamps,\n i.e. some time stamps contradict the order of events.\n\nInductive visual Miner can reorder the events and discover a new model.\nWould you like to do that?", "Illogical Time Stamps", 0, 2, (Icon)null, options, options[0]);
                return true;//n == 1 ? true : false;
            }
        });
        this.sortEvents.setOnComplete(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().setData("Log attributes", state);
            }
        });
        this.sortEvents.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().invalidate("Log attributes");
            }
        });
        chain.addConnection(this.gatherAttributes, this.sortEvents);
        this.makeLog.setOnComplete(new Runnable() {
            public void run() {
                //panel.getTraceView().set(state.getLog(), state.getTraceColourMap());
            }
        });
        chain.addConnection(this.sortEvents, this.makeLog);
        chain.addConnection(this.makeLog, this.filterLogOnActivities);
        this.mine.setOnComplete(new Runnable() {
            public void run() {
                //panel.getSaveModelButton().setEnabled(true);
                //panel.getEditModelView().setModel(state.getModel());
            }
        });
        this.mine.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getSaveModelButton().setEnabled(false);
                //panel.getEditModelView().setMessage("Mining tree...");
                state.setSelection(new Selection());
            }
        });
        chain.addConnection(this.filterLogOnActivities, this.mine);
        this.layoutModel.setOnComplete(new Runnable() {
            public void run() {
                //panel.getGraph().changeDot(state.getDot(), state.getSVGDiagram(), true);
            }
        });
        chain.addConnection(this.mine, this.layoutModel);
        this.align.setOnComplete(new Runnable() {
            public void run() {
                //panel.getSaveLogButton().setEnabled(true);
                //panel.getTraceView().set(state.getModel(), state.getIvMLog(), state.getSelection(), state.getTraceColourMap());
                //PopupPopulator.updatePopup(panel, state);
            }
        });
        this.align.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getSaveLogButton().setEnabled(false);
                //PopupPopulator.updatePopup(panel, state);
            }
        });
        chain.addConnection(this.mine, this.align);
        this.layoutAlignment.setOnStart(new Runnable() {
            public void run() {
                if (!state.getMode().isShowDeviations()) {
                    state.removeModelAndLogMovesSelection();
                }

            }
        });
        this.layoutAlignment.setOnComplete(new Runnable() {
            public void run() {
                //panel.getGraph().changeDot(state.getDot(), state.getSVGDiagram(), true);
                //InductiveVisualMinerController.makeElementsSelectable(state.getVisualisationInfo(), panel, state.getSelection());
                //panel.getTraceView().setEventColourMap(state.getTraceViewColourMap());
            }
        });
        chain.addConnection(this.layoutModel, this.layoutAlignment);
        chain.addConnection(this.align, this.layoutAlignment);
        this.ivmAttributes.setOnComplete(new Runnable() {
            public void run() {
                state.getHighlightingFiltersController().setAttributesInfo(state.getIvMAttributesInfo());
                //panel.getTraceColourMapView().setAttributes(state.getIvMAttributesInfo());
            }
        });
        this.ivmAttributes.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getTraceColourMapView().invalidateAttributes();
            }
        });
        chain.addConnection(this.align, this.ivmAttributes);
        chain.addConnection(this.align, this.animationScaler);
        this.animate.setOnStart(new Runnable() {
            public void run() {
                //InductiveVisualMinerController.setAnimationStatus(panel, " ", false);
            }
        });
        this.animate.setOnComplete(new Runnable() {
            public void run() {
                if (state.getAnimationGraphVizTokens() != null) {
                    //panel.getGraph().setTokens(state.getAnimationGraphVizTokens());
                    //panel.getGraph().setAnimationExtremeTimes(state.getAnimationScaler().getMinInUserTime(), state.getAnimationScaler().getMaxInUserTime());
                    //panel.getGraph().setFilteredLog(state.getIvMLogFiltered());
                    //panel.getGraph().setAnimationEnabled(true);
                } else {
                    System.out.println("animation disabled");
                    //InductiveVisualMinerController.setAnimationStatus(panel, "animation disabled", true);
                    //panel.getGraph().setAnimationEnabled(false);
                }

                //panel.repaint();
                //state.setHistogramWidth((int)panel.getGraph().getControlsProgressLine().getWidth());
            }
        });
        this.animate.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getGraph().setAnimationEnabled(false);
                //InductiveVisualMinerController.setAnimationStatus(panel, " ", false);
            }
        });
        chain.addConnection(this.animationScaler, this.animate);
        chain.addConnection(this.layoutAlignment, this.animate);
        this.traceColouring.setOnComplete(new Runnable() {
            public void run() {
                //panel.getGraph().setTraceColourMap(state.getTraceColourMap());
                //panel.getTraceView().setTraceColourMap(state.getTraceColourMap());
                //panel.getTraceView().repaint();
                //panel.repaint();
            }
        });
        chain.addConnection(this.ivmAttributes, this.traceColouring);
        /*
        this.filterNodeSelection.setOnComplete(new Runnable() {
            public void run() {
                //HighlightingFiltersView.updateSelectionDescription(panel, state.getSelection(), state.getHighlightingFiltersController(), state.getModel());
                //panel.getTraceView().set(state.getModel(), state.getIvMLogFiltered(), state.getSelection(), state.getTraceColourMap());
                //PopupPopulator.updatePopup(panel, state);
                // panel.getGraph().setFilteredLog(state.getIvMLogFiltered());
                //panel.repaint();
            }
        });
        this.filterNodeSelection.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getGraph().setFilteredLog((IvMLogFilteredImpl)null);
                //PopupPopulator.updatePopup(panel, state);
                //panel.getGraph().repaint();
            }
        });
        chain.addConnection(this.layoutAlignment, this.filterNodeSelection);
        chain.addConnection(this.ivmAttributes, this.filterNodeSelection);
        */
        this.performance.setOnComplete(new Runnable() {
            public void run() {
                //PopupPopulator.updatePopup(panel, state);

                try {
                    //InductiveVisualMinerController.updateHighlighting(panel, state);
                } catch (UnknownTreeNodeException var2) {
                    var2.printStackTrace();
                }

                //panel.getGraph().repaint();
            }
        });
        this.performance.setOnInvalidate(new Runnable() {
            public void run() {
                //PopupPopulator.updatePopup(panel, state);
                //panel.getGraph().repaint();
            }
        });
        /*
        chain.addConnection(this.animationScaler, this.performance);
        chain.addConnection(this.filterNodeSelection, this.performance);
        this.histogram.setOnComplete(new Runnable() {
            public void run() {
                //panel.getGraph().setHistogramData(state.getHistogramData());
                //panel.getGraph().repaint();
            }
        });
        chain.addConnection(this.animationScaler, this.histogram);
        chain.addConnection(this.filterNodeSelection, this.histogram);
        this.dataAnalysisTrace.setOnComplete(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().setData("Trace attributes", state);
            }
        });
        this.dataAnalysisTrace.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().invalidate("Trace attributes");
            }
        });
        chain.addConnection(this.filterNodeSelection, this.dataAnalysisTrace);
        this.dataAnalysisEvent.setOnComplete(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().setData("Event attributes", state);
            }
        });
        this.dataAnalysisEvent.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().invalidate("Event attributes");
            }
        });
        chain.addConnection(this.filterNodeSelection, this.dataAnalysisEvent);

         */
        this.dataAnalysisCohort.setOnComplete(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().setData("Cohort analysis", state);
            }
        });
        this.dataAnalysisCohort.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().invalidate("Cohort analysis");
            }
        });
        chain.addConnection(this.makeLog, this.dataAnalysisCohort);
        this.dataAnalysisLog.setOnComplete(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().setData("Log attributes", state);
            }
        });
        this.dataAnalysisLog.setOnInvalidate(new Runnable() {
            public void run() {
                //panel.getDataAnalysesView().setData("Log attributes", state);
            }
        });
        //chain.addConnection(this.filterNodeSelection, this.dataAnalysisLog);
        chain.addConnection(this.histogram, this.done);
        chain.addConnection(this.performance, this.done);
        chain.addConnection(this.traceColouring, this.done);
        chain.addConnection(this.animate, this.done);
        chain.addConnection(this.dataAnalysisTrace, this.done);
        chain.addConnection(this.dataAnalysisEvent, this.done);
        chain.addConnection(this.dataAnalysisCohort, this.done);
        chain.addConnection(this.dataAnalysisLog, this.done);
        return chain;
    }

    @Override
    public List<VisualMinerWrapper> getDiscoveryTechniques() {
        return null;
    }

    @Override
    public VisualMinerWrapper[] getDiscoveryTechniquesArray() {
        return new VisualMinerWrapper[0];
    }

    @Override
    public List<PreMiningFilter> getPreMiningFilters() {
        return null;
    }

    @Override
    public List<HighlightingFilter> getHighlightingFilters() {
        return null;
    }

    @Override
    public List<Mode> getModes() {
        return null;
    }

    @Override
    public Mode[] getModesArray() {
        return new Mode[0];
    }

    @Override
    public List<PopupItemActivity> getPopupItemsActivity() {
        return null;
    }

    @Override
    public List<PopupItemStartEnd> getPopupItemsStartEnd() {
        return null;
    }

    @Override
    public List<PopupItemLogMove> getPopupItemsLogMove() {
        return null;
    }

    @Override
    public List<PopupItemModelMove> getPopupItemsModelMove() {
        return null;
    }

    @Override
    public List<PopupItemLog> getPopupItemsLog() {
        return null;
    }

    @Override
    public List<DataAnalysisTableFactory> getDataAnalysisTables() {
        return null;
    }

    @Override
    public IvMVirtualAttributeFactory getVirtualAttributes() {
        return null;
    }

    @Override
    public InductiveVisualMinerState getState() {
        return this.getState();
    }

    @Override
    public InductiveVisualMinerPanel getPanel() {
        return null;
    }

    @Override
    public Chain<InductiveVisualMinerState> getChain() {
        return this.getChain();
    }

    @Override
    public AlignmentComputer getAlignmentComputer() {
        return null;
    }
}
